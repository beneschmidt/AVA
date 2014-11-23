package com.ava;

import java.util.Map;

import com.ava.menu.ObserverMenu;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeListReader;
import com.ava.node.NodeType;

/**
 * Oberserver that connects to all possible sockets in the net and asking them about a specific rumor
 */
public class ObserverStartup {

	public static void main(String[] args) {
		Map<Integer, NodeDefinition> nodes = NodeListReader.readNodeDefinitionsFromFile(args[0]);
		NodeDefinition nodeDefinition = new NodeDefinition(0, "localhost", 12000, NodeType.observer);
		Node node = new Node(nodeDefinition);

		node.startServerAsThread();

		ObserverMenu menu = new ObserverMenu(node, nodes);
		menu.run();

		//		node.connectToOtherNodes(nodes.values());
		//
		//		SocketMessage socketMessage = SocketMessageFactory.createSystemMessage().setForwardingType(SocketMessageForwardingType.back_to_sender)
		//				.setNode(node.getNodeDefinition()).setMessage(args[1]).setAction(SocketMessageAction.rumor_check);
		//		node.broadcastMessage(socketMessage);
		//
		//		RumorChecker rumorChecked = RumorChecker.getInstance();
		//		while (rumorChecked.getCheckedNodesCount() != node.getConnectedSockets().size()) {
		//			System.out.println(rumorChecked.getCheckedNodesCount() + "/" + node.getConnectedSockets().size());
		//		}
		//
		//		System.out.println(rumorChecked.getCheckedNodesCount() + "/" + node.getConnectedSockets().size());
		//		FileWriterHelper helper = new FileWriterHelper("auswertung.txt");
		//		helper.writeToFile(rumorChecked.toString());
		//
		//		node.closeAllConnections();
		//		System.exit(0);
	}
}
