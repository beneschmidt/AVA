package com.ava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.utils.TimeUtils;

/**
 * Thread to read input from a socket
 * 
 * @author Benne
 */
public class SocketInputReader extends Thread {

	public static final String RUMOR = "RUMOR=";
	public static final String EXIT = "EXIT";
	public static final String EXIT_OTHERS = "EXIT_OTHERS";

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
				System.out.println("[IN] " + currentTime + ": " + message.getNode().getPort() + "=" + message.getMessage());

				handleMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessage(SocketMessage message) {
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
				int i = 0;
				for (Map.Entry<NodeDefinition, Socket> nextEntry : node.getConnectedSockets().entrySet()) {
					if (!nextEntry.getKey().equals(message.getNode())) {
						nextTargets.put(nextEntry.getKey(), nextEntry.getValue());
						if (i >= 1) {
							break;
						} else {
							i++;
						}
					}
				}
				break;
			}
			case broadcast_to_all_but_two: {
				int i = node.getConnectedSockets().size();
				for (Map.Entry<NodeDefinition, Socket> nextEntry : node.getConnectedSockets().entrySet()) {
					if (!nextEntry.getKey().equals(message.getNode())) {
						nextTargets.put(nextEntry.getKey(), nextEntry.getValue());
						if (i <= 1) {
							break;
						} else {
							i--;
						}
					}
				}
				break;
			}
			default: {
				break;
			}
		}
		if (message.getMessage().equals(EXIT)) {
			node.sendMessage(nextTargets, message);
			node.closeServer();
		} else if (message.getMessage().equals(EXIT_OTHERS)) {
			node.sendMessage(nextTargets, message);
		} else if (message.getMessage().startsWith(RUMOR)) {
			int rumorCount = Rumors.getInstance().addRumor(message.getNode(), message.getMessage());
			if (rumorCount < 3) {
				System.out.println(rumorCount + ", I don't believe it yet..." + Rumors.getInstance().getRumor(message.getMessage()));
				message.setNode(node.getNodeDefinition());
				node.sendMessage(nextTargets, message);
			} else {
				System.out.println("the rumors are true, " + message.getMessage());
			}
		}
	}
}
