package com.ava.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.NodeServer;
import com.ava.ueb01.NodeDefinition;
import com.ava.utils.FileReaderHelper;
import com.ava.utils.ResourceHelper;

public class Node implements NodeServer {

	private final NodeDefinition nodeDefinition;
	private List<Socket> connectedSockets;
	private ServerSocket serverSocket;

	public Node(NodeDefinition nodeDefinition) {
		this.nodeDefinition = nodeDefinition;
		this.connectedSockets = new LinkedList<Socket>();
	}

	public NodeDefinition getNodeDefinition() {
		return nodeDefinition;
	}

	public static void main(String[] args) {
		try {
			System.out.println("Argumente:" + Arrays.toString(args));
			Map<Integer, NodeDefinition> nodes = readNodesFromFile(args[0]);
			NodeDefinition nodeDefinition = askForNodeToWorkWith(nodes);
			System.out.println("Mein Node: " + nodeDefinition);
			System.out.println("Los gehts!");
			final Node node = new Node(nodeDefinition);
			new Thread(new Runnable() {
				public void run() {
					node.startServer();
				}
			}).start();
			;
			node.connectToOtherNodes(nodes);
			node.broadcastMessage();
			Thread.sleep(10000);
			node.closeAllConnections();
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n");
			e.printStackTrace();
		}
	}

	// TOOO: connection behalten und verwalten
	public void connectToOtherNodes(Map<Integer, NodeDefinition> allNodes) {
		for (NodeDefinition nextDef : allNodes.values()) {
			if (nextDef.getId() != nodeDefinition.getId()) {
				boolean connected = false;
				while (!connected) {
					try {
						Socket socket = new Socket(nextDef.getIp(), nextDef.getPort());
						connectedSockets.add(socket);
						System.out.println("Verbindung hergestellt: " + nextDef);
						connected = true;
					} catch (Exception e) {
						System.err.println("Verbindung mit port " + nextDef.getPort() + " fehlgeschlagen!");
					}
				}
			}
		}
	}

	@Override
	public void broadcastMessage() {
		for (Socket nextSocket : connectedSockets) {
			OutputStreamWriter writer = null;
			try {
				OutputStream out = nextSocket.getOutputStream();
				writer = new OutputStreamWriter(out);
				writer.write("Hallo, ich bin " + nodeDefinition);
			} catch (Exception e) {
				System.err.println("Verbindung fehlgeschlagen!");
			} finally {
				ResourceHelper.close(writer);
			}
		}
	}

	@Override
	public void startServer() {
		try {
			serverSocket = new ServerSocket(nodeDefinition.getPort());

			while (true) {
				final Socket clientSocket = serverSocket.accept();
				System.out.print("Server has connected!\n");

				Thread thread = new SocketInputReader(clientSocket);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Whoops! It didn't work!\n");
		}
	}

	@Override
	public void closeServer() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			ResourceHelper.close(serverSocket);
		}
	}

	public void closeAllConnections() {
		try {
			for (Socket socket : connectedSockets) {
				ResourceHelper.close(socket);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Fraegt uber die Konsole die Node-ID ab und sucht aus den Nodes den entsprechenden Eintrag
	 * @param nodes
	 * @return gesuchten Node
	 * @throws Exception
	 */
	private static NodeDefinition askForNodeToWorkWith(Map<Integer, NodeDefinition> nodes) throws Exception {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter id:");
			String inputLine = br.readLine();
			try {
				Integer id = Integer.parseInt(inputLine);
				if (nodes.get(id) != null) {
					return nodes.get(id);
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		throw new Exception("nothing found!");
	}

	/**
	 * Liest alle Node-Definitionen aus einer gegebenen Datei aus
	 * @param filename
	 * @return Map mit Nodes
	 */
	private static Map<Integer, NodeDefinition> readNodesFromFile(String filename) {
		FileReaderHelper fileReaderHelper = new FileReaderHelper(filename);
		Map<Integer, NodeDefinition> nodes = new TreeMap<Integer, NodeDefinition>();
		List<String> lines = fileReaderHelper.readFileAsRows();
		for (String nextLine : lines) {
			NodeDefinition nodeDefinition = new NodeDefinition(nextLine);
			nodes.put(nodeDefinition.getId(), nodeDefinition);
			System.out.println(nextLine);
		}
		System.out.println("Insgesamt gefunden: " + nodes.size());
		return nodes;
	}
}
