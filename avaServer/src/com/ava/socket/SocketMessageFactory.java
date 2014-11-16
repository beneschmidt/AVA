package com.ava.socket;

import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessage.SocketMessageSource;

public class SocketMessageFactory {

	public static SocketMessage createUserMessage() {
		return new SocketMessage().setSource(SocketMessageSource.user).setForwardingType(SocketMessageForwardingType.none);
	}

	public static SocketMessage createSystemMessage() {
		return new SocketMessage().setSource(SocketMessageSource.system).setForwardingType(SocketMessageForwardingType.none);
	}

	public static SocketMessage createUserMessage(NodeDefinition initator, NodeDefinition node, String message, SocketMessageAction action) {
		return new SocketMessage(SocketMessageSource.user, SocketMessageForwardingType.none, initator, node, message, action);
	}

	public static SocketMessage createSystemMessage(NodeDefinition initator, NodeDefinition node, String message, SocketMessageAction action) {
		return new SocketMessage(SocketMessageSource.system, SocketMessageForwardingType.none, initator, node, message, action);
	}

	public static SocketMessage createForwardingUserMessage(NodeDefinition initator, SocketMessageForwardingType forwardingType, NodeDefinition node, String message,
			SocketMessageAction action) {
		return new SocketMessage(SocketMessageSource.user, forwardingType, initator, node, message, action);
	}

	public static SocketMessage createForwardingSystemMessage(NodeDefinition initator, SocketMessageForwardingType forwardingType, NodeDefinition node, String message,
			SocketMessageAction action) {
		return new SocketMessage(SocketMessageSource.system, forwardingType, initator, node, message, action);
	}
}
