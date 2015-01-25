package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.ava.Statistics;
import com.ava.advertisement.ItemStatistics;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeType;
import com.ava.socket.RumorStatistics;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;
import com.ava.utils.DoubleCountHelper;
import com.ava.utils.DoubleCountHelper.Count;
import com.ava.utils.FileWriterHelper;
import com.ava.utils.TimeUtils;

/**
 * special menu for observer. the user can choose which observer scenario should run
 */
public class ObserverMenu implements Menu {

	/**
	 * enum for possible menu selections, containing an ID and a display text
	 * @author D063416
	 *
	 */
	private enum MenuPoint {
		rumor(1, "Rumor observer"), business(2, "business observer"), exit(0, "exit");

		private final int id;
		private final String text;

		private MenuPoint(int value, String text) {
			this.id = value;
			this.text = text;
		}

		public int getId() {
			return id;
		}

		public String getText() {
			return text;
		}

		public static MenuPoint getById(int id) {
			for (MenuPoint point : values()) {
				if (point.getId() == id) {
					return point;
				}
			}
			return null;
		}

		public String toString() {
			return new StringBuilder().append(getId()).append(") ").append(getText()).append("\n").toString();
		}
	}

	private Node node;
	/** all neighbours of the node */
	private Map<Integer, NodeDefinition> nodes;

	public ObserverMenu(Node node, Map<Integer, NodeDefinition> nodes) {
		this.node = node;
		this.nodes = nodes;
	}

	@Override
	public Object run() {
		while (true) {
			System.out.println(toString());
			MenuPoint menuPoint = readInput();
			handleInput(menuPoint);
		}
	}

	/**
	 * acts according to the selected menu point. the system may be close through selecting the exit menu
	 * @param menuPoint
	 */
	private void handleInput(MenuPoint menuPoint) {
		switch (menuPoint) {
			case rumor: {
				checkStatisticsAtNodesAndWriteToFile(RumorStatistics.getInstance(), nodes);
				break;
			}
			case business: {
				Map<Integer, NodeDefinition> customers = new TreeMap<Integer, NodeDefinition>();
				for (NodeDefinition node : nodes.values()) {
					if (node.getNodeType() == NodeType.customer) {
						customers.put(node.getId(), node);
					}
				}
				checkStatisticsAtNodesAndWriteToFile(ItemStatistics.getInstance(), customers);
				break;
			}
			default: {
				System.out.println("EXIT");
				System.exit(0);
				break;
			}
		}
	}

	/**
	 * waits till all connected nodes are checked and then writes the statistics to a file. Finally all connections are closed
	 */
	private void checkStatisticsAtNodesAndWriteToFile(Statistics statistics, Map<Integer, NodeDefinition> nodes) {
		node.connectToOtherNodes(nodes.values());

		// ask for message to send to the nodes
		MessageMenu messageMenu = new MessageMenu();
		String message = (String) messageMenu.run();

		// create new message
		SocketMessage socketMessage = SocketMessageFactory.createSystemMessage().setForwardingType(SocketMessageForwardingType.back_to_sender)
				.setNode(node.getNodeDefinition()).setMessage(message).setAction(statistics.getMessageAction());

		// check outgoing and incoming for the first time
		int s1 = node.broadcastMessage(socketMessage);
		int sleepTime = 200;
		TimeUtils.sleep(sleepTime);
		int r1 = statistics.checkedNodesCount();

		Count count = new Count(s1, r1);
		DoubleCountHelper doubleCountHelper = new DoubleCountHelper();
		doubleCountHelper.addCount(count);

		// check again as long as the last two tests are not completely equal
		boolean checkFinished = false;
		while (!checkFinished) {
			statistics.clear();
			int send = node.broadcastMessage(socketMessage);
			TimeUtils.sleep(sleepTime);
			int received = statistics.checkedNodesCount();

			Count nextCount = new Count(send, received);
			doubleCountHelper.addCount(nextCount);
			checkFinished = doubleCountHelper.allEqual();
		}

		FileWriterHelper helper = new FileWriterHelper(statistics.getFilePrefix() + "_" + message + ".txt");
		String statisticString = statistics.toString();
		helper.writeToFile(statisticString);

		System.out.println(statisticString);

		node.closeAllConnections();
	}

	private MenuPoint readInput() {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter selection:");
			try {
				String inputLine = br.readLine();
				Integer id = Integer.parseInt(inputLine);
				MenuPoint menuPoint = MenuPoint.getById(id);
				if (menuPoint != null) {
					return menuPoint;
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder menu = new StringBuilder().append("What do you want to do?\n");
		for (MenuPoint menuPoint : MenuPoint.values()) {
			menu.append(menuPoint.toString());
		}
		return menu.toString();
	}
}
