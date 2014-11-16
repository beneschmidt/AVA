package com.ava.utils;

import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.ava.node.NodeDefinition;

public class NodeConnector {

	private int connectionTries;

	public NodeConnector() {
		this(10);
	}

	public NodeConnector(Integer connectionTries) {
		this.connectionTries = connectionTries;
	}

	public Map<NodeDefinition, Socket> connectToOtherNodes(Integer ownId, Collection<NodeDefinition> nodesToConnect) {
		Map<NodeDefinition, Socket> connectedSockets = new TreeMap<NodeDefinition, Socket>();
		for (NodeDefinition nextDef : nodesToConnect) {
			if (nextDef.getId() != ownId) {
				boolean connected = false;
				int connectionTriesSoFar = 0;
				while (!connected && connectionTriesSoFar < this.connectionTries) {
					try {
						Socket socket = new Socket(nextDef.getIp(), nextDef.getPort());
						connectedSockets.put(nextDef, socket);
						System.out.println("Verbindung hergestellt: " + nextDef);
						connected = true;
					} catch (Exception e) {
						System.err.println("Verbindung mit port " + nextDef.getPort() + " fehlgeschlagen!" + e.getMessage());
						connectionTriesSoFar++;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		return connectedSockets;
	}
}
