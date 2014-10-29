package com.ava.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.ueb01.NodeDefinition;
import com.ava.utils.FileReaderHelper;

public class Node {

	private final NodeDefinition nodeDefinition;

	public Node(NodeDefinition nodeDefinition) {
		this.nodeDefinition = nodeDefinition;
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
			Node node = new Node(nodeDefinition);
			node.startServer();
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n");
			e.printStackTrace();
		}
	}

	public void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(nodeDefinition.getPort());
			Socket skt = serverSocket.accept();
			System.out.print("Server has connected!\n");
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
			out.close();
			skt.close();
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Whoops! It didn't work!\n");
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
