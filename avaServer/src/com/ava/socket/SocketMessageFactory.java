package com.ava.socket;

import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessage.SocketMessageSource;

public class SocketMessageFactory {

	public static SocketMessage createUserMessage(NodeDefinition node, String message) {
		return new SocketMessage(SocketMessageSource.user, SocketMessageForwardingType.none, node, message);
	}

	public static SocketMessage createSystemMessage(NodeDefinition node, String message) {
		return new SocketMessage(SocketMessageSource.system, SocketMessageForwardingType.none, node, message);
	}

	public static SocketMessage createForwardingUserMessage(SocketMessageForwardingType forwardingType, NodeDefinition node, String message) {
		return new SocketMessage(SocketMessageSource.user, forwardingType, node, message);
	}

	public static SocketMessage createForwardingSystemMessage(SocketMessageForwardingType forwardingType, NodeDefinition node, String message) {
		return new SocketMessage(SocketMessageSource.system, forwardingType, node, message);
	}
}
