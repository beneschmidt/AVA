package com.ava.node;

import java.util.Map;

public class NodeFactory {

	/**
	 * creates a node for the given type
	 * @param nodeDefinition
	 * @return new node
	 */
	public static Node createNode(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes) {
		switch (nodeDefinition.getNodeType()) {
			case business: {
				return new BusinessNode(nodeDefinition, nodes);
			}
			case customer: {
				return new CustomerNode(nodeDefinition, nodes);
			}
			case observer: {
				return new Node(nodeDefinition, nodes);
			}
			case resourceHandlerA: {
				return new ResourceHandlerNode(nodeDefinition, nodes, "a");
			}
			case resourceHandlerB: {
				return new ResourceHandlerNode(nodeDefinition, nodes, "b");
			}
			case resourceWriter: {
				return new ResourceWriterNode(nodeDefinition, nodes);
			}
			default: {
				return null;
			}
		}
	}
}
