package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.ava.advertisement.ItemStatistics;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeType;
import com.ava.socket.RumorChecker;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;
import com.ava.utils.FileWriterHelper;

public class ObserverMenu implements Menu {

	private enum MenuPoint {
		rumor(1, "Rumor observer"), business(2, "business observer"), exit(0, "exit");

		private final int value;
		private final String text;

		private MenuPoint(int value, String text) {
			this.value = value;
			this.text = text;
		}

		public int getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static MenuPoint getById(int id) {
			for (MenuPoint point : values()) {
				if (point.getValue() == id) {
					return point;
				}
			}
			return null;
		}

		public String toString() {
			return new StringBuilder().append(getValue()).append(") ").append(getText()).append("\n").toString();
		}
	}

	private Node node;
	private Map<Integer, NodeDefinition> nodes;

	public ObserverMenu(Node node, Map<Integer, NodeDefinition> nodes) {
		this.node = node;
		this.nodes = nodes;
	}

	@Override
	public Object run() {
		while (true) {
			System.out.println(toString());
			MenuPoint o = readInput();
			handleInput(o);
		}
	}

	private void handleInput(MenuPoint o) {
		switch (o) {
			case rumor: {
				node.connectToOtherNodes(nodes.values());
				MessageMenu messageMenu = new MessageMenu();
				String message = (String) messageMenu.run();

				SocketMessage socketMessage = SocketMessageFactory.createSystemMessage().setForwardingType(SocketMessageForwardingType.back_to_sender)
						.setNode(node.getNodeDefinition()).setMessage(message).setAction(SocketMessageAction.rumor_check);
				node.broadcastMessage(socketMessage);

				RumorChecker rumorChecked = RumorChecker.getInstance();
				while (rumorChecked.getCheckedNodesCount() != node.getConnectedSockets().size()) {
					System.out.println(rumorChecked.getCheckedNodesCount() + "/" + node.getConnectedSockets().size());
				}

				System.out.println(rumorChecked.getCheckedNodesCount() + "/" + node.getConnectedSockets().size());
				FileWriterHelper helper = new FileWriterHelper("auswertung.txt");
				helper.writeToFile(rumorChecked.toString());

				node.closeAllConnections();
				break;
			}
			case business: {
				Map<Integer, NodeDefinition> customers = new TreeMap<Integer, NodeDefinition>();
				for (NodeDefinition node : nodes.values()) {
					if (node.getNodeType() == NodeType.customer) {
						customers.put(node.getId(), node);
					}
				}

				node.connectToOtherNodes(customers.values());
				MessageMenu messageMenu = new MessageMenu();
				String message = (String) messageMenu.run();

				SocketMessage socketMessage = SocketMessageFactory.createSystemMessage().setForwardingType(SocketMessageForwardingType.back_to_sender)
						.setNode(node.getNodeDefinition()).setMessage(message).setAction(SocketMessageAction.checkItemBought);
				node.broadcastMessage(socketMessage);

				ItemStatistics statistics = ItemStatistics.getInstance();
				while (statistics.checkedNodesCount() != node.getConnectedSockets().size()) {
					System.out.println(statistics.checkedNodesCount() + "/" + node.getConnectedSockets().size());
				}
				System.out.println(statistics.checkedNodesCount() + "/" + node.getConnectedSockets().size());
				FileWriterHelper helper = new FileWriterHelper(message + "_itemStatistics.txt");
				helper.writeToFile(statistics.toString());

				node.closeAllConnections();
				break;
			}
			default: {
				node.closeAllConnections();
				System.out.println("EXIT");
				System.exit(0);
				break;
			}
		}
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
