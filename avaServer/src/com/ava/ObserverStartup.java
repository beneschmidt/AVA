package com.ava;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.socket.RumorChecked;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;
import com.ava.utils.FileReaderHelper;
import com.ava.utils.FileWriterHelper;

public class ObserverStartup {

	// 0 localhost:12000
	public static void main(String[] args) {
		Map<Integer, NodeDefinition> nodes = readNodeDefinitionsFromFile(args[0]);
		NodeDefinition nodeDefinition = new NodeDefinition("0 localhost:12000");
		Node node = new Node(nodeDefinition);

		node.startServerAsThread();
		node.connectToOtherNodes(nodes.values());
		SocketMessage socketMessage = SocketMessageFactory.createForwardingSystemMessage(SocketMessageForwardingType.back_to_sender, node.getNodeDefinition(),
				args[1], SocketMessageAction.rumor_check);
		node.broadcastMessage(socketMessage);

		RumorChecked rumorChecked = RumorChecked.getInstance();
		while (rumorChecked.getCheckedNodesCount() != node.getConnectedSockets().size()) {
			System.out.println(rumorChecked.getCheckedNodesCount()+"/"+node.getConnectedSockets().size());
		}

		System.out.println(rumorChecked.getCheckedNodesCount()+"/"+node.getConnectedSockets().size());
		FileWriterHelper helper = new FileWriterHelper("auswertung.txt");
		helper.writeToFile(rumorChecked.toString());
		
		node.closeAllConnections();
		System.exit(0);
	}

	/**
	 * Liest alle Node-Definitionen aus einer gegebenen Datei aus
	 * 
	 * @param filename
	 * @return Map mit Nodes
	 */
	private static Map<Integer, NodeDefinition> readNodeDefinitionsFromFile(String filename) {
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
