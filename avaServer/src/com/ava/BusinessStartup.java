package com.ava;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ava.advertisement.AdvertisementMessageList;
import com.ava.advertisement.BoughtItems;
import com.ava.advertisement.PurchaseDecisionMessageList;
import com.ava.graph.GraphInformation;
import com.ava.graph.NodeGraph;
import com.ava.menu.MainMenu;
import com.ava.menu.SimpleNodeSelectionMenu;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeFactory;
import com.ava.node.NodeListReader;
import com.ava.node.NodeType;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessageFactory;
import com.ava.utils.BusinessPropertiesReader;
import com.ava.utils.FileReaderHelper;

/**
 * starting point for the business nodes scenarion. A business oder customer node is created and all neighbours are intially connected.
 *
 */
public class BusinessStartup {

	public static void main(String[] args) {
		try {
			System.out.println("Arguments:" + Arrays.toString(args));
			if (args.length < 2) {
				System.out.println("usage: java -jar {jarname} {node definition file} {graph file} {optional: node-id}");
			}

			initMaxValues();

			Map<Integer, NodeDefinition> nodes = NodeListReader.readNodeDefinitionsFromFile(args[0]);
			NodeDefinition nodeDefinition = null;
			if (args.length == 2) {
				SimpleNodeSelectionMenu menu = new SimpleNodeSelectionMenu(nodes);
				nodeDefinition = (NodeDefinition) menu.run();
			} else {
				nodeDefinition = nodes.get(Integer.parseInt(args[2]));
			}
			System.out.println("I am: " + nodeDefinition);
			NodeGraph graph = createNodeGraph(nodes, args[1]);
			Node node = NodeFactory.createNode(nodeDefinition, nodes);
			// singleton update
			GraphInformation.getInstance().setFullGraph(graph);

			List<NodeDefinition> neighbours = graph.getDefinitionsForNode(nodeDefinition.getId());
			logNeighbours(neighbours);

			node.startServerAsThread();
			node.connectToOtherNodes(neighbours);
			SocketMessage socketMessage = SocketMessageFactory.createSystemMessage().setNode(node.getNodeDefinition())
					.setMessage("hey I'm " + node.getNodeDefinition()).setAction(SocketMessageAction.simple);
			node.broadcastMessage(socketMessage);

			if (node.getNodeDefinition().getNodeType() == NodeType.business) {
				MainMenu mainMenu = new MainMenu(node);
				mainMenu.run();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void initMaxValues() throws FileNotFoundException, IOException {
		Properties businessProperties = BusinessPropertiesReader.readProperties();
		BoughtItems.getInstance().setMax(Integer.parseInt(businessProperties.getProperty("maxBoughtItems")));
		AdvertisementMessageList.getInstance().setMax(Integer.parseInt(businessProperties.getProperty("maxAd")));
		PurchaseDecisionMessageList.getInstance().setMax(Integer.parseInt(businessProperties.getProperty("maxPurchase")));
	}

	private static void logNeighbours(List<NodeDefinition> neighbours) {
		StringBuilder string = new StringBuilder("Neighbours: ");
		for (NodeDefinition nextDefinition : neighbours) {
			string.append(nextDefinition.getId()).append(", ");
		}
		System.out.println(string.toString());
	}

	private static NodeGraph createNodeGraph(Map<Integer, NodeDefinition> nodes, String fileName) {
		FileReaderHelper helper = new FileReaderHelper(fileName);
		List<String> fileContent = helper.readFileAsRows();
		NodeGraph nodeGraph = new NodeGraph(nodes, fileContent);
		return nodeGraph;
	}
}
