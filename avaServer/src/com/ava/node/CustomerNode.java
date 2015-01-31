package com.ava.node;

import java.util.Map;

public class CustomerNode extends Node {

	public static final int adMessageLimit = 3;
	public static final int purchaseDecisionLimit = 3;

	public CustomerNode(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes) {
		super(nodeDefinition, nodes);
	}

}
