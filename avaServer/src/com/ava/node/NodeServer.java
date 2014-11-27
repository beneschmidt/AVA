package com.ava.node;

import java.util.Collection;

import com.ava.socket.SocketMessage;

/**
 * interface to specify functions a node server has to implement
 */
public interface NodeServer {

	void startServerAsThread();

	void closeServer();

	void connectToOtherNodes(Collection<NodeDefinition> othersNodes);

	int broadcastMessage(SocketMessage message);

	boolean sendMessage(NodeDefinition node, SocketMessage message);
}
