package com.ava.socket;

import com.ava.node.NodeDefinition;
import com.google.gson.Gson;

public class SocketMessage {
	
	public static final int SYSTEM = 1;
	public static final int USER = 2;

	private int type;
	private NodeDefinition node;
	private String message;

	public static SocketMessage fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SocketMessage.class);
	}

	public SocketMessage() {
	}

	SocketMessage(int type, NodeDefinition node, String message) {
		this.type = type;
		this.node = node;
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public NodeDefinition getNode() {
		return node;
	}

	public void setNode(NodeDefinition node) {
		this.node = node;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String asJson() {
		String json = new Gson().toJson(this);
		return json;
	}

	@Override
	public String toString() {
		return "SocketMessage [type=" + type + ", node=" + node + ", message=" + message + "]";
	}

}
