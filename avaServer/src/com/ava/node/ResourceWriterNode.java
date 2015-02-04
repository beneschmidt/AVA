package com.ava.node;

import java.util.Map;

import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessage.SocketMessageForwardingType;
import com.ava.socket.SocketMessageFactory;

public class ResourceWriterNode extends Node {

	private ResourceHandlerLink firstHandler;
	private ResourceHandlerLink secondHandler;
	private ResourceType resourceType;
	private boolean jobDone;

	private enum ResourceType {
		even, odd
	}

	public ResourceWriterNode(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes) {
		super(nodeDefinition, nodes);
		NodeDefinition resourceHandlerA = null;
		NodeDefinition resourceHandlerB = null;
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
		if (resourceType == ResourceType.odd) {
			firstHandler = new ResourceHandlerLink(resourceHandlerA, "a.txt");
			secondHandler = new ResourceHandlerLink(resourceHandlerB, "b.txt");
		} else {
			firstHandler = new ResourceHandlerLink(resourceHandlerB, "b.txt");
			secondHandler = new ResourceHandlerLink(resourceHandlerA, "a.txt");
		}
	}

	@Override
	public void startServerAsThread() {
		super.startServerAsThread();
		getFirstHandlerAccess();
	}

	public void getFirstHandlerAccess() {
		getAccess(getFirstHandler().getHandler());
	}

	public void getSecondHandlerAccess() {
		getAccess(getSecondHandler().getHandler());
	}

	public ResourceHandlerLink getFirstHandler() {
		return firstHandler;
	}

	public ResourceHandlerLink getSecondHandler() {
		return secondHandler;
	}

	private void getAccess(NodeDefinition handler) {
		System.out.println("Try to get access for " + handler.getNodeType());
		SocketMessage message = SocketMessageFactory.createSystemMessage(this.getNodeDefinition(), this.getNodeDefinition(), "hello",
				SocketMessageAction.getAccess).setForwardingType(SocketMessageForwardingType.back_to_sender);
		this.sendSingleMessage(handler, message);
	}

	public int getFirstChangedNumber(int initialNumber) {
		return ++initialNumber;
	}

	public int getSecondChangedNumber(int initialNumber) {
		return --initialNumber;
	}

	public boolean isHandlerForFirstStep(String accessFile) {
		return (resourceType == ResourceType.odd && accessFile.equals("a.txt")) || (resourceType == ResourceType.even && accessFile.equals("b.txt"));
	}

	public boolean isJobDone() {
		return jobDone;
	}

	public void setJobDone(boolean jobDone) {
		this.jobDone = jobDone;
	}

	public static class ResourceHandlerLink {
		private NodeDefinition handler;
		private String fileName;

		public ResourceHandlerLink(NodeDefinition handler, String fileName) {
			this.handler = handler;
			this.fileName = fileName;
		}

		@Override
		public String toString() {
			return "ResourceHandlerLink [resourceHandlerA=" + handler + ", fileName=" + fileName + "]";
		}

		public NodeDefinition getHandler() {
			return handler;
		}

		public void setHandler(NodeDefinition handler) {
			this.handler = handler;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

	}

}
