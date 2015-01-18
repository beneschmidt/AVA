package com.ava.node;

public class NodeFactory {

	/**
	 * creates a node for the given type
	 * @param nodeDefinition
	 * @return new node
	 */
	public static Node createNode(NodeDefinition nodeDefinition) {
		switch (nodeDefinition.getNodeType()) {
			case business: {
				return new BusinessNode(nodeDefinition);
			}
			case customer: {
				return new CustomerNode(nodeDefinition);
			}
			case observer: {
				return new Node(nodeDefinition);
			}
			default: {
				return null;
			}
		}
	}
}
