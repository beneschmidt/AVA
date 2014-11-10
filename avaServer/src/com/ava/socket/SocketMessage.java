package com.ava.socket;

import com.ava.node.NodeDefinition;
import com.google.gson.Gson;

/**
 * A socket message that is transfered from one node to another. Following information is given: <br>
 * - message source type<br>
 * - forwarding type<br>
 * - node that sent the message (last sender)<br>
 * - message to deliver (string)<br>
 * <br>
 */
public class SocketMessage {

	public enum SocketMessageSource {
		system, user;
	}

	public enum SocketMessageForwardingType {
		none, broadcast, broadcast_without_sender, broadcast_to_two, broadcast_to_all_but_two, broadcast_to_half;
	}

	private SocketMessageSource source;
	private SocketMessageForwardingType forwardingType;
	private NodeDefinition node;
	private String message;

	/**
	 * The message can be created by parsing a json String
	 * @param json
	 * @return SocketMessage
	 */
	public static SocketMessage fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SocketMessage.class);
	}

	public SocketMessage() {
	}

	SocketMessage(SocketMessageSource source, SocketMessageForwardingType forwardingType, NodeDefinition node, String message) {
		this.source = source;
		this.node = node;
		this.message = message;
		this.forwardingType = forwardingType;
	}

	public SocketMessageSource getSource() {
		return source;
	}

	public void setSource(SocketMessageSource source) {
		this.source = source;
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

	public SocketMessageForwardingType getForwardingType() {
		return forwardingType;
	}

	public void setForwardingType(SocketMessageForwardingType forwardingType) {
		this.forwardingType = forwardingType;
	}

	public String asJson() {
		String json = new Gson().toJson(this);
		return json;
	}

	@Override
	public String toString() {
		return "SocketMessage [source=" + source + ", forwardingType=" + forwardingType + ", node=" + node + ", message=" + message + "]";
	}

}
