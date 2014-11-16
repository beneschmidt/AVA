package com.ava.node;

public class CustomerNode extends Node{

	public static final int adMessageLimit = 3;
	public static final int purchaseDecisionLimit = 3;
	
	
	public CustomerNode(NodeDefinition nodeDefinition) {
		super(nodeDefinition);
	}

}
