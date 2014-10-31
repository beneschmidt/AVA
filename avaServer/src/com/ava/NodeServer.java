package com.ava;

import java.util.Map;

import com.ava.ueb01.NodeDefinition;

public interface NodeServer {

	void startServer();

	void closeServer();

	void connectToOtherNodes(Map<Integer, NodeDefinition> othersNodes);
	
	void broadcastMessage();
}
