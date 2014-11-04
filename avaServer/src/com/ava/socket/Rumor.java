package com.ava.socket;

import java.util.Set;
import java.util.TreeSet;

import com.ava.node.NodeDefinition;

/**
 * a rumor, that is spreading around. It has a message and knows about all nodes that have heard of it
 * @author Benedikt Schmidt
 */
public class Rumor implements Comparable<Rumor> {

	private String message;
	private Set<NodeDefinition> nodes;

	public Rumor(String message) {
		nodes = new TreeSet<NodeDefinition>();
		this.message = message;
	}

	public int iHeardThatToo(NodeDefinition node) {
		nodes.add(node);
		return getReceiveCount();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getReceiveCount() {
		return nodes.size();
	}

	@Override
	public String toString() {
		return "Rumor [message=" + message + ", nodes=" + nodes + "]";
	}

	@Override
	public int compareTo(Rumor o) {
		return message.compareTo(o.getMessage());
	}

}
