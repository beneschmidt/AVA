package com.ava;

import java.util.Arrays;
import java.util.Map;

import com.ava.menu.SimpleNodeSelectionMenu;
import com.ava.node.Node;
import com.ava.node.NodeDefinition;
import com.ava.node.NodeListReader;

public class BusinessStartup {

	public static void main(String[] args) {
		Node node = null;
		try {
			System.out.println("Arguments:" + Arrays.toString(args));
			if (args.length < 2) {
				System.out.println("usage: java -jar {jarname} {node definition file} {graph file} {optional: node-id}");
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
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
