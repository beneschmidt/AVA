package com.ava.node;

import java.util.LinkedList;
import java.util.Map;

public class ResourceHandlerNode extends Node {

	private String fileName;
	private LinkedList<NodeDefinition> accessQueue;
	private NodeDefinition currentlyBlocking = null;

	public ResourceHandlerNode(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes, String fileName) {
		super(nodeDefinition, nodes);
		this.fileName = fileName + ".txt";
		accessQueue = new LinkedList<>();
	}

	@Override
	public void startServerAsThread() {
		super.startServerAsThread();
	}

	public boolean getAccess(NodeDefinition nodeDefinition) {
		if (currentlyBlocking != null) {
			accessQueue.add(nodeDefinition);
			return false;
		} else {
			currentlyBlocking = nodeDefinition;
			return true;
		}
	}

	/**
	 * two possibilities: the node is currently blocking or the node is in queue. First way is to set the blocking node to null, second way is to remove the node from the queue
	 * @param nodeDefinition
	 */
	public void releaseAccess(NodeDefinition nodeDefinition) {
		if (currentlyBlocking != null && currentlyBlocking.equals(nodeDefinition)) {
			currentlyBlocking = null;
		} else if (accessQueue.contains(nodeDefinition)) {
			accessQueue.remove(nodeDefinition);
		}
	}

	public boolean nextAccessPossible() {
		return moreElementsInQueue() && currentlyBlocking == null;
	}

	public boolean moreElementsInQueue() {
		return accessQueue.size() != 0;
	}

	public boolean isCurrentlyBlocked() {
		return currentlyBlocking != null;
	}

	public NodeDefinition grantNextAccess() {
		if (nextAccessPossible()) {
			NodeDefinition nextNode = accessQueue.pop();
			currentlyBlocking = nextNode;
			return nextNode;
		} else {
			throw new IndexOutOfBoundsException("There's no node in queue or it is blocked");
		}
	}

	public LinkedList<NodeDefinition> getAccessQueue() {
		return accessQueue;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "ResourceHandlerNode [fileName=" + fileName + ", accessQueue=" + accessQueue.size() + "]";
	}

	public NodeDefinition getCurrentlyBlocking() {
		return currentlyBlocking;
	}

}
