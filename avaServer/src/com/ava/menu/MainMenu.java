package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.ava.node.BusinessNode;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;

/**
 * main menu that is the central point of action where you can choose from all possible sub menus
 */
public class MainMenu implements Menu {
	private enum MenuPoint {
		closeCon(1, "close connections to other sockets"), closeConeAndExit(2, "close connections and exit"), sendSingle(3, "send a message to a socket"), sendBroadcast(
				4, "send a message to all sockets"), closeAllSockets(5, "close all sockets"), spread_rumor(6, "spread a rumor"), new_product(7,
				"create new product and send it");

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

	public MainMenu(Node node) {
		this.node = node;
	}

	@Override
	public Object run() {
		while (true) {
			System.out.println(toString());
			MenuPoint o = readInput();
			handleInput(o);
		}
	}

	private void handleInput(MenuPoint menuPoint) {
		try {
			switch (menuPoint) {
				case closeCon: {
					node.closeAllConnections();
					break;
				}
				case closeConeAndExit: {
					node.closeAllConnections();
					node.closeServer();
					System.exit(0);
				}
				case sendSingle: {
					NodeSelectionMenu menu = new NodeSelectionMenu(node);
					NodeDefinition nodeToMessageTo = (NodeDefinition) menu.run();
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					SocketMessage socketMessage = SocketMessageFactory.createUserMessage(node.getNodeDefinition(), node.getNodeDefinition(), message,
							SocketMessageAction.simple);
					node.sendMessage(nodeToMessageTo, socketMessage);
					break;
				}
				case sendBroadcast: {
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					SocketMessage socketMessage = SocketMessageFactory.createUserMessage(node.getNodeDefinition(), node.getNodeDefinition(), message,
							SocketMessageAction.simple);
					node.broadcastMessage(socketMessage);
					break;
				}
				case closeAllSockets: {
					String message = "Please exit";
					SocketMessage socketMessage = SocketMessageFactory.createForwardingUserMessage(node.getNodeDefinition(),
							SocketMessageForwardingType.broadcast, node.getNodeDefinition(), message, SocketMessageAction.exit);
					node.broadcastMessage(socketMessage);
					node.closeServer();
					break;
				}
				case spread_rumor: {
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					String realMessage = message;

					ForwardingMenu forwardingMenu = new ForwardingMenu();
					SocketMessageForwardingType type = (SocketMessageForwardingType) forwardingMenu.run();

					SocketMessage socketMessage = SocketMessageFactory.createForwardingUserMessage(node.getNodeDefinition(), type, node.getNodeDefinition(),
							realMessage, SocketMessageAction.rumor);
					node.broadcastMessage(socketMessage);
					break;
				}
				case new_product: {
					if (node instanceof BusinessNode) {
						BusinessNode bNode = (BusinessNode) node;
						if (bNode.getEtat() <= 0) {
							System.out.println("You don't have any etat");
						} else {
							MessageMenu messageMenu = new MessageMenu();
							String message = (String) messageMenu.run();
							String realMessage = message;
							SocketMessage socketMessage = SocketMessageFactory.createUserMessage().setAction(SocketMessageAction.advertisement)
									.setForwardingType(SocketMessageForwardingType.broadcast_without_sender).setInitiator(node.getNodeDefinition())
									.setNode(node.getNodeDefinition()).setMessage(realMessage);
							node.broadcastMessage(socketMessage);
							bNode.adPlaced();
						}
					} else {
						System.out.println("You can't start a campaign! You're not a business");
					}
					break;
				}
				default:
					System.out.println("wat?");
					break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
