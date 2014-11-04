package com.ava.socket;

import com.ava.node.NodeDefinition;

public class SocketMessageFactory {

	public static SocketMessage createUserMessage(NodeDefinition node, String message){
		return new SocketMessage(SocketMessage.USER, node, message);
	}
	
	public static SocketMessage createSystemMessage(NodeDefinition node, String message){
		return new SocketMessage(SocketMessage.SYSTEM, node, message);
	}
}
