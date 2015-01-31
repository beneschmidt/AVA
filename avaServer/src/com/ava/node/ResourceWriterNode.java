package com.ava.node;

import java.util.Map;

import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;

public class ResourceWriterNode extends Node {

	private NodeDefinition resourceHandlerA;
	private NodeDefinition resourceHandlerB;
	private ResourceType resourceType;

	private enum ResourceType {
		even, odd
	}

	public ResourceWriterNode(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes) {
		super(nodeDefinition, nodes);
		for (NodeDefinition nextNode : nodes.values()) {
			if (nextNode.getNodeType() == NodeType.resourceHandlerA) {
				resourceHandlerA = nextNode;
			} else if (nextNode.getNodeType() == NodeType.resourceHandlerB) {
				resourceHandlerB = nextNode;
			}
		}
		if (resourceHandlerA == null || resourceHandlerB == null) {
			throw new RuntimeException("One of the resourceHandlers can not be obtained");
		}

		resourceType = (nodeDefinition.getId() % 2 != 0) ? ResourceType.odd : ResourceType.even;
	}

	@Override
	public void startServerAsThread() {
		super.startServerAsThread();
		if (resourceType == ResourceType.odd) {
			getAccess(resourceHandlerA);
			getAccess(resourceHandlerB);
		} else {
			getAccess(resourceHandlerB);
			getAccess(resourceHandlerA);
		}
	}

	private void getAccess(NodeDefinition handler) {
		System.out.println("Try to get access for " + handler.getNodeType());
		SocketMessage message = SocketMessageFactory.createSystemMessage(this.getNodeDefinition(), this.getNodeDefinition(), "hello",
				SocketMessageAction.getAccess).setForwardingType(SocketMessageForwardingType.back_to_sender);
		this.sendSingleMessage(handler, message);
	}

	public int getChangedNumber(int initialNumber, String accessFile) {
		if ((resourceType == ResourceType.odd && accessFile.equals("a.txt")) || (resourceType == ResourceType.even && accessFile.equals("b.txt"))) {
			return ++initialNumber;
		} else {
			return --initialNumber;
		}
	}

}
