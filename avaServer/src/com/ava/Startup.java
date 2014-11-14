package com.ava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ava.graph.NodeGraph;
import com.ava.menu.MainMenu;
import com.ava.menu.SimpleNodeSelectionMenu;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeListReader;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessageFactory;
import com.ava.utils.FileReaderHelper;

/**
 * main start class of the whole programm
 */
public class Startup {

	public static void main(String[] args) {
		Node node = null;
		try {
			System.out.println("Arguments:" + Arrays.toString(args));
			if (args.length < 2) {
				System.out.println("usage: java -jar {jarname} {node definition file} {graph file} {optional: node-id}");
				System.exit(0);
			}
			Map<Integer, NodeDefinition> nodes = NodeListReader.readNodeDefinitionsFromFile(args[0]);
			NodeDefinition nodeDefinition = null;
			if (args.length == 2) {
				SimpleNodeSelectionMenu menu = new SimpleNodeSelectionMenu(nodes);
				nodeDefinition = (NodeDefinition) menu.run();
			} else {
				nodeDefinition = nodes.get(Integer.parseInt(args[2]));
			}
			System.out.println("I am: " + nodeDefinition);
			node = new Node(nodeDefinition);
			List<NodeDefinition> neighbours = loadOwnNeighboursFromFile(nodeDefinition.getId(), nodes, args[1]);
			logNeighbours(neighbours);

			node.startServerAsThread();
			node.connectToOtherNodes(neighbours);
			SocketMessage socketMessage = SocketMessageFactory.createSystemMessage()
					.setNode(node.getNodeDefinition())
					.setMessage("hey I'm " + node.getNodeDefinition())
					.setAction(SocketMessageAction.simple);
			node.broadcastMessage(socketMessage);

			// main thread runs the main menu
			MainMenu mainMenu = new MainMenu(node);
			mainMenu.run();
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n");
			e.printStackTrace();
		} finally {
			if (node != null) {
				node.closeAllConnections();
			}
		}
	}

	private static void logNeighbours(List<NodeDefinition> neighbours) {
		StringBuilder string = new StringBuilder("Neighbours: ");
		for (NodeDefinition nextDefinition : neighbours) {
			string.append(nextDefinition.getId()).append(", ");
		}
		System.out.println(string.toString());
	}

	private static List<NodeDefinition> loadOwnNeighboursFromFile(Integer ownId, Map<Integer, NodeDefinition> nodes, String fileName) {
		FileReaderHelper helper = new FileReaderHelper(fileName);
		List<String> fileContent = helper.readFileAsRows();
		NodeGraph nodeGraph = new NodeGraph(nodes, fileContent);
		return nodeGraph.getDefinitionsForNode(ownId);
	}

}
