package com.ava;

import java.util.Map;

import com.ava.menu.ObserverMenu;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeListReader;
import com.ava.node.NodeType;

/**
 * Oberserver that connects to all possible sockets in the net. After creation of a menu the user 
 * is asked to specify which observer scenario should run
 */
public class ObserverStartup {

	public static void main(String[] args) {
		Map<Integer, NodeDefinition> nodes = NodeListReader.readNodeDefinitionsFromFile(args[0]);
		NodeDefinition nodeDefinition = new NodeDefinition(0, "localhost", 12000, NodeType.observer);
		Node node = new Node(nodeDefinition, nodes);

		node.startServerAsThread();

		ObserverMenu menu = new ObserverMenu(node, nodes);
		menu.run();
	}
}
