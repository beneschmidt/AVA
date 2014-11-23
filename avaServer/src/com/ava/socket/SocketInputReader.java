package com.ava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import com.ava.advertisement.AdvertisementMessageList;
import com.ava.advertisement.BoughtItems;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.utils.TimeUtils;

/**
 * Thread to read input from a socket and then handle it
 */
public class SocketInputReader extends Thread {

	private Socket socket;
	private Node node;

	public SocketInputReader(Node node, Socket socket) {
		this.socket = socket;
		this.node = node;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				String currentTime = TimeUtils.getCurrentTimeString();
				SocketMessage message = SocketMessage.fromJson(inputLine);
				System.out.println("[-->] " + currentTime + ": " + message.getNode().getPort() + "=" + message.getMessage());

				handleMessage(message);
			}
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
			case rumor_check: {
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
				RumorChecker.getInstance().rumorCheckedAtNode(message.getNode(), Boolean.valueOf(message.getMessage()));
				break;
			}
			case closed: {
				node.getConnectedSockets().remove(message.getNode());
				break;
			}
			case advertisement: {
				if (BoughtItems.getInstance().canIBuyThat(message)) {
					boolean shouldBuy = AdvertisementMessageList.getInstance().iHeardThatAndIWonderedIfIShouldBuy(message);
					if (shouldBuy) {
						BoughtItems.getInstance().itemBought(message);
						AdvertisementMessageList.getInstance().clearHistoryForMessage(message);
					}
				} else {
					// termination
				}
				// TODO kaufen und enstprechend reagieren
				// TODO irgendwie weiterleiten
				break;
			}
			case purchaseDecision: {
				boolean shouldBuy = AdvertisementMessageList.getInstance().iHeardThatAndIWonderedIfIShouldBuy(message);
				if (shouldBuy) {
					// TODO kaufen und enstprechend reagieren
				} else {
					// TODO irgendwie weiterleiten
				}
				break;
			}
			default: {
				node.sendMessage(nextTargets, message);
			}
		}
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
				for (Map.Entry<NodeDefinition, Socket> nextEntry : node.getConnectedSockets().entrySet()) {
					if (!nextEntry.getKey().equals(message.getNode())) {
						nextTargets.put(nextEntry.getKey(), nextEntry.getValue());
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
					backSocket = socket;
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
