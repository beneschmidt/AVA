package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketInputReader;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;

public class MainMenu implements Menu {
	private static final int SPREAD_RUMOR = 6;
	private static final int CLOSE_ALL_SOCKETS = 5;
	private static final int SEND_BROADCAST_MESSAGE = 4;
	private static final int SEND_SINGLE_MESSAGE = 3;
	private static final int CLOSE_CON_EXIT = 2;
	private static final int CLOSE_CON = 1;
	private static Map<Integer, String> selections;
	static {
		selections = new TreeMap<>();
		selections.put(CLOSE_CON, "close connections to other sockets");
		selections.put(CLOSE_CON_EXIT, "close connections and exit");
		selections.put(SEND_SINGLE_MESSAGE, "send a message to a socket");
		selections.put(SEND_BROADCAST_MESSAGE, "send a message to all sockets");
		selections.put(CLOSE_ALL_SOCKETS, "close all sockets");
		selections.put(SPREAD_RUMOR, "spread a rumor");
	}

	private Node node;

	public MainMenu(Node node) {
		this.node = node;
	}

	@Override
	public Object run() {
		while (true) {
			System.out.println(toString());
			Object o = readInput();
			handleInput(o);
		}
	}

	private void handleInput(Object o) {
		try {
			Integer idToHandle = Integer.valueOf(o.toString());
			switch (idToHandle) {
				case CLOSE_CON: {
					node.closeAllConnections();
					break;
				}
				case CLOSE_CON_EXIT: {
					node.closeAllConnections();
					node.closeServer();
					System.exit(0);
				}
				case SEND_SINGLE_MESSAGE: {
					NodeSelectionMenu menu = new NodeSelectionMenu(node);
					NodeDefinition nodeToMessageTo = (NodeDefinition) menu.run();
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					SocketMessage socketMessage = SocketMessageFactory.createUserMessage(node.getNodeDefinition(), message);
					node.sendMessage(nodeToMessageTo, socketMessage);
					break;
				}
				case SEND_BROADCAST_MESSAGE: {
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					SocketMessage socketMessage = SocketMessageFactory.createUserMessage(node.getNodeDefinition(), message);
					node.broadcastMessage(socketMessage);
					break;
				}
				case CLOSE_ALL_SOCKETS: {
					String message = SocketInputReader.EXIT;
					SocketMessage socketMessage = SocketMessageFactory.createForwardingUserMessage(SocketMessageForwardingType.broadcast,
							node.getNodeDefinition(), message);
					node.broadcastMessage(socketMessage);
					node.closeServer();
				}
				case SPREAD_RUMOR: {
					MessageMenu messageMenu = new MessageMenu();
					String message = (String) messageMenu.run();
					String realMessage = SocketInputReader.RUMOR + message;
					SocketMessage socketMessage = SocketMessageFactory.createForwardingUserMessage(SocketMessageForwardingType.broadcast_without_sender,
							node.getNodeDefinition(), realMessage);
					node.broadcastMessage(socketMessage);
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

	private Integer readInput() {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter selection:");
			try {
				String inputLine = br.readLine();
				Integer id = Integer.parseInt(inputLine);
				if (selections.containsKey(id)) {
					return id;
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder menu = new StringBuilder().append("What do you want to do?\n");
		for (Map.Entry<Integer, String> entries : selections.entrySet()) {
			menu.append(entries.getKey()).append(") ").append(entries.getValue()).append("\n");
		}
		return menu.toString();
	}
}
