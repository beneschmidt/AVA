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
		none, broadcast, broadcast_without_sender, broadcast_to_two, broadcast_to_all_but_two, broadcast_to_half, back_to_sender;
	}

	public enum SocketMessageAction {
		exit, rumor, simple, checkRumor, rumor_checked, closed, advertisement, purchaseDecision, itemBought, checkItemBought,
		itemBoughtChecked, explorer, echo, clearEcho, connectToMe, checkMessageCount, messageCountChecked, listNeighbours, getAccess, accessGranted, releaseAccess,
		resourceCurrentlyLocked, checkForDeadlock;
	}

	private SocketMessageSource source;
	private SocketMessageForwardingType forwardingType;
	private NodeDefinition node;
	private NodeDefinition initiator;
	private String message;
	private SocketMessageAction action;

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

	SocketMessage(SocketMessageSource source, SocketMessageForwardingType forwardingType, NodeDefinition initiator, NodeDefinition node, String message,
			SocketMessageAction action) {
		this.source = source;
		this.node = node;
		this.message = message;
		this.forwardingType = forwardingType;
		this.action = action;
		this.initiator = initiator;
	}

	public SocketMessageSource getSource() {
		return source;
	}

	public SocketMessage setSource(SocketMessageSource source) {
		this.source = source;
		return this;
	}

	public NodeDefinition getNode() {
		return node;
	}

	public SocketMessage setNode(NodeDefinition node) {
		this.node = node;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public SocketMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public SocketMessageForwardingType getForwardingType() {
		return forwardingType;
	}

	public SocketMessage setForwardingType(SocketMessageForwardingType forwardingType) {
		this.forwardingType = forwardingType;
		return this;
	}

	public SocketMessageAction getAction() {
		return action;
	}

	public SocketMessage setAction(SocketMessageAction action) {
		this.action = action;
		return this;
	}

	public String asJson() {
		String json = new Gson().toJson(this);
		return json;
	}

	public NodeDefinition getInitiator() {
		return initiator;
	}

	public SocketMessage setInitiator(NodeDefinition initiator) {
		this.initiator = initiator;
		return this;
	}

	@Override
	public String toString() {
		return "SocketMessage [source=" + source + ", forwardingType=" + forwardingType + ", node=" + node + ", message=" + message + ", action=" + action
				+ "]";
	}

}
