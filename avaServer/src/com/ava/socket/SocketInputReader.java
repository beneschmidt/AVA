package com.ava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.advertisement.AdvertisementMessageList;
import com.ava.advertisement.BoughtItems;
import com.ava.advertisement.BusinessMessageList;
import com.ava.advertisement.ItemStatistics;
import com.ava.advertisement.PurchaseDecisionMessageList;
import com.ava.graph.GraphInformation;
import com.ava.graph.GraphNodeCombination;
import com.ava.node.BusinessNode;
import com.ava.node.CustomerNode;
import com.ava.node.EchoStatus;
import com.ava.node.EchoStatus.EchoColor;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.utils.EchoAnalysis;

/**
 * Thread to read input from a socket and then handle it
 */
public class SocketInputReader extends Thread {

	private Socket socket;
	private Node node;
	private BoughtItems alreadyBoughtItems;
	private Object lockObject = new Object();

	public SocketInputReader(Node node, Socket socket) {
		this.socket = socket;
		this.node = node;
		alreadyBoughtItems = BoughtItems.getInstance();
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				SocketMessage message = SocketMessage.fromJson(inputLine);
				synchronized (lockObject) {
					handleMessage(message);
				}
			}
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * input message handling
	 * @param message
	 */
	private void handleMessage(SocketMessage message) {
		Map<NodeDefinition, Socket> nextTargets = pickNextTargets(message);
		switch (message.getAction()) {
			case exit: {
				node.sendMessage(nextTargets, message);
				node.closeServer();
				break;
			}
			case rumor: {
				// add rumor to the rumor list
				int rumorCount = Rumors.getInstance().addRumor(message.getNode(), message.getMessage());
				if (rumorCount < 3) {
					Rumor rumor = Rumors.getInstance().getRumor(message.getMessage());
					// if it wasn't heard of enough nodes, send it forward to more nodes
					System.out.println(rumorCount + ", I don't believe it yet..." + rumor);
					message.setNode(node.getNodeDefinition());
					node.sendMessage(nextTargets, message);
				} else {
					// if enough nodes told the rumor, it must be true
					System.out.println("the rumors are true, " + message.getMessage());
				}
				break;
			}
			case checkRumor: {
				Rumor rumor = Rumors.getInstance().getRumor(message.getMessage());
				if (rumor != null) {
					int rumorCount = rumor.getReceiveCount();
					message.setMessage((rumorCount >= 3) + "");
				} else {
					message.setMessage(false + "");
				}
				message.setNode(node.getNodeDefinition());
				message.setAction(SocketMessageAction.rumor_checked);
				message.setForwardingType(SocketMessageForwardingType.none);
				node.sendMessage(nextTargets, message);
				break;
			}
			case rumor_checked: {
				RumorStatistics.getInstance().rumorCheckedAtNode(message.getNode(), Boolean.valueOf(message.getMessage()));
				break;
			}
			case closed: {
				node.getConnectedSockets().remove(message.getNode());
				break;
			}
			case advertisement: {
				reactToBusinessMessage(message, AdvertisementMessageList.getInstance());
				break;
			}
			case purchaseDecision: {
				reactToBusinessMessage(message, PurchaseDecisionMessageList.getInstance());
				break;
			}
			case itemBought: {
				System.out.println(message.getNode().getId() + " bought an item: " + message.getMessage());
				try {
					synchronized (node.getConnectedSockets()) {
						if (!node.getConnectedSockets().containsKey(message.getNode())) {
							node.connectionToNode(message.getNode());
							System.out.println(message.getNode().getId() + " is a new customer");
							SocketMessage connectToMeMessage = SocketMessageFactory.createSystemMessage(node.getNodeDefinition(), node.getNodeDefinition(), "",
									SocketMessageAction.connectToMe);
							node.sendMessage(message.getNode(), connectToMeMessage);
							GraphNodeCombination newNeighbourCombination = new GraphNodeCombination(node.getNodeDefinition().getId(), message.getNode().getId());
							GraphInformation.getInstance().getFullGraph().addCombination(newNeighbourCombination);
						}
					}
					BusinessNode bNode = (BusinessNode) node;
					Thread.sleep(1000);
					if (bNode.getEtat() > 0) {
						System.out.println("Still have " + bNode.getEtat() + ", going to spend it!");
						SocketMessage socketMessage = SocketMessageFactory.createUserMessage().setAction(SocketMessageAction.advertisement)
								.setForwardingType(SocketMessageForwardingType.broadcast_without_sender).setInitiator(node.getNodeDefinition())
								.setNode(node.getNodeDefinition()).setMessage(message.getMessage());
						node.broadcastMessage(socketMessage);
						bNode.adPlaced();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			case checkItemBought: {
				int buyCount = alreadyBoughtItems.buyCount(message);
				message.setMessage(buyCount + "");
				message.setNode(node.getNodeDefinition());
				message.setAction(SocketMessageAction.itemBoughtChecked);
				message.setForwardingType(SocketMessageForwardingType.none);
				node.sendMessage(nextTargets, message);
				break;
			}
			case itemBoughtChecked: {
				ItemStatistics.getInstance().itemsCheckedAtNode(message.getNode(), Integer.parseInt(message.getMessage()));
				break;
			}
			case explorer: {
				handleExplorerEcho(message, nextTargets);
				break;
			}
			case echo: {
				handleExplorerEcho(message, nextTargets);
				break;
			}
			case clearEcho: {
				synchronized (node.getEchoStatus()) {
					if (!node.getEchoStatus().isClear()) {
						node.getEchoStatus().clear();
						System.out.println("Echo was cleared");
						message.setNode(node.getNodeDefinition());
						node.sendMessage(nextTargets, message);
					}
				}
				break;
			}
			case connectToMe: {
				try {
					synchronized (node.getConnectedSockets()) {
						if (!node.getConnectedSockets().containsKey(message.getNode())) {
							node.connectionToNode(message.getNode());
							System.out.println(message.getNode().getId() + " is a new neighbour");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			default: {
				node.sendMessage(nextTargets, message);
			}
		}
	}

	/**
	 * check if I'm a customer node, then check if I can still buy items, then check if I should buy item (depends on how much messages of that type I already got),
	 * then comes the big part: buy item, clear message list for that type, inform seller, send purchase decision to neighbours and get a new neighbour
	 * Also possibly advertise to neighbours if needed
	 * @param message
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	private void reactToBusinessMessage(SocketMessage message, BusinessMessageList list) {
		if (node instanceof CustomerNode) {
			synchronized (alreadyBoughtItems) {
				if (alreadyBoughtItems.canIBuyThat(message)) {
					boolean shouldBuy = list.iHeardThatAndIWonderedIfIShouldBuy(message);
					if (shouldBuy) {
						try {
							alreadyBoughtItems.itemBought(message);
							list.clearHistoryForMessage(message);
							sendBoughtItemMessageToInitiator(message);
							System.out.println("I bought: " + message.getMessage() + " for the " + alreadyBoughtItems.buyCount(message) + ". time");
							sendNeighbourMessage(message, SocketMessageAction.purchaseDecision);
							getNewNeighbourForNode(node);
						} catch (IndexOutOfBoundsException e) {
							System.out.println("no shoving pls, can't buy more than " + alreadyBoughtItems.getMax());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (list.shouldIAdvertiseFurtherMore()) {
						//						System.out.println("I'm gonna tell my friends to buy: " + message.getMessage() + ", " + list.currentCount(message));
						sendNeighbourMessage(message, SocketMessageAction.advertisement);
					}
				}
			}
		}
	}

	private synchronized void handleExplorerEcho(SocketMessage message, Map<NodeDefinition, Socket> nextTargets) {
		synchronized (node.getEchoStatus()) {
			EchoStatus echoStatus = node.getEchoStatus();
			echoStatus.increaseCount();
			echoStatus.loadIds(message.getMessage());
			echoStatus.addId(message.getNode().getId());
			message.setMessage(echoStatus.getIds().toString());

			if (echoStatus.getColor() == EchoColor.white) {
				echoStatus.setColor(EchoColor.red);
				echoStatus.setFirstNeighbour(message.getNode());
				message.setNode(node.getNodeDefinition());
				node.sendMessage(nextTargets, message);
				System.out.println("Echo-Algo was initiated by " + echoStatus.getFirstNeighbour().getId() + ", now I'm red. All neighbours were informed");
			}
			System.out.println(message.getAction() + " from " + message.getNode().getId() + ", reached " + echoStatus.getCount() + "/"
					+ node.getConnectedSockets().size() + ", ids so far: " + echoStatus.getIds().toString());
			if (node.getConnectedSockets().size() == echoStatus.getCount()) {
				echoStatus.setColor(EchoColor.green);
				System.out.println("All echos and explorer received, now I'm green");
				if (echoStatus.isInitiator()) {
					// stop echo and analyse
					echoStatus.stopped();
					System.out.println("FINISHED: " + new EchoAnalysis(echoStatus).toString());

					// now clear echo on all nodes
					echoStatus.clear();
					SocketMessage socketMessage = SocketMessageFactory.createForwardingSystemMessage(node.getNodeDefinition(),
							SocketMessageForwardingType.broadcast_without_sender, node.getNodeDefinition(), "", SocketMessageAction.clearEcho);
					node.broadcastMessage(socketMessage);
				} else {
					message.setAction(SocketMessageAction.echo);
					message.setNode(node.getNodeDefinition());
					node.sendMessage(echoStatus.getFirstNeighbour(), message);
					System.out.println("Send message back to " + echoStatus.getFirstNeighbour());
				}
			}
		}
	}

	private void getNewNeighbourForNode(Node node) {
		GraphInformation nodeInfo = GraphInformation.getInstance();
		synchronized (node.getConnectedSockets()) {
			List<NodeDefinition> notExistingNodes = nodeInfo.getFullGraph().getDefinitionsExceptForNode(node.getNodeDefinition().getId());
			try {
				NodeDefinition newDef = notExistingNodes.get(0);
				node.connectionToNode(newDef);
				SocketMessage connectToMeMessage = SocketMessageFactory.createSystemMessage(node.getNodeDefinition(), node.getNodeDefinition(), "",
						SocketMessageAction.connectToMe);
				node.sendMessage(newDef, connectToMeMessage);
				GraphNodeCombination newNeighbourCombination = new GraphNodeCombination(node.getNodeDefinition().getId(), newDef.getId());
				nodeInfo.getFullGraph().addCombination(newNeighbourCombination);
				System.out.println("NEW NEIGHBOUR: " + newDef);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendBoughtItemMessageToInitiator(SocketMessage message) throws UnknownHostException, IOException {
		Socket socket = new Socket(message.getInitiator().getIp(), message.getInitiator().getPort());
		SocketOutputWriter writer = new SocketOutputWriter();
		SocketMessage newMessage = SocketMessageFactory.createUserMessage().setAction(SocketMessageAction.itemBought).setInitiator(node.getNodeDefinition())
				.setForwardingType(SocketMessageForwardingType.none).setNode(node.getNodeDefinition()).setMessage(message.getMessage());
		writer.writeMessage(socket, newMessage);
		socket.close();
	}

	private void sendNeighbourMessage(SocketMessage message, SocketMessageAction action) {
		SocketMessage newPurchasedMessage = SocketMessageFactory.createUserMessage().setAction(action).setInitiator(message.getInitiator())
				.setForwardingType(SocketMessageForwardingType.broadcast_without_sender).setNode(node.getNodeDefinition()).setMessage(message.getMessage());
		node.broadcastMessage(newPurchasedMessage);
	}

	/**
	 * pick the next node targets to send the next message to
	 * @param message
	 * @return all the next nodes (definition and socket)
	 */
	private Map<NodeDefinition, Socket> pickNextTargets(SocketMessage message) {
		Map<NodeDefinition, Socket> nextTargets = new TreeMap<NodeDefinition, Socket>();
		switch (message.getForwardingType()) {
			case none: {
				break;
			}
			case broadcast: {
				nextTargets = node.getConnectedSockets();
				break;
			}
			case broadcast_without_sender: {
				synchronized (node.getConnectedSockets()) {
					for (Map.Entry<NodeDefinition, Socket> nextEntry : node.getConnectedSockets().entrySet()) {
						if (!nextEntry.getKey().equals(message.getNode())) {
							nextTargets.put(nextEntry.getKey(), nextEntry.getValue());
						}
					}
				}
				break;
			}
			case broadcast_to_two: {
				nextTargets = pickNodesFromNodeList(message, 2);
				break;
			}
			case broadcast_to_all_but_two: {
				// exlude the last two nodes
				int i = node.getConnectedSockets().size();
				int numberOfNodes = i - 2;
				nextTargets = pickNodesFromNodeList(message, numberOfNodes);
				break;
			}
			case broadcast_to_half: {
				int max = (node.getConnectedSockets().size() - 1) / 2;
				nextTargets = pickNodesFromNodeList(message, max);
				break;
			}
			case back_to_sender: {
				Socket backSocket = null;
				if (node.getConnectedSockets().containsKey(message.getNode())) {
					backSocket = node.getConnectedSockets().get(message.getNode());
				} else {
					try {
						node.connectionToNode(message.getNode());
						backSocket = node.getConnectedSockets().get(message.getNode());
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				nextTargets.put(message.getNode(), backSocket);
				break;
			}
			default: {
				break;
			}
		}
		return nextTargets;
	}

	/**
	 * picks specific nodes from the node list. The number of nodes to pick is given. If the number is higher than the actual
	 * number of nodes or equal to it, then all nodes are returned.
	 * The algorithm runs straight through the map regarding the sorting. There is no random filtering.
	 * @param message
	 * @param numberOfNodes
	 * @return nodes
	 */
	private Map<NodeDefinition, Socket> pickNodesFromNodeList(SocketMessage message, int numberOfNodes) {
		if (numberOfNodes >= node.getConnectedSockets().size()) {
			return node.getConnectedSockets();
		}

		Map<NodeDefinition, Socket> nextTargets = new TreeMap<NodeDefinition, Socket>();
		int i = 0;
		for (Map.Entry<NodeDefinition, Socket> nextEntry : node.getConnectedSockets().entrySet()) {
			if (!nextEntry.getKey().equals(message.getNode())) {
				nextTargets.put(nextEntry.getKey(), nextEntry.getValue());
				if (i >= numberOfNodes) {
					break;
				} else {
					i++;
				}
			}
		}
		return nextTargets;
	}
}
