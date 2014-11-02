package com.ava.client;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ava.NodeServer;
import com.ava.ueb01.NodeDefinition;
import com.ava.utils.ResourceHelper;

public class Node implements NodeServer {

    private final NodeDefinition nodeDefinition;
    private List<Socket> connectedSockets;
    private ServerSocket serverSocket;

    public Node(NodeDefinition nodeDefinition) {
	this.nodeDefinition = nodeDefinition;
	this.connectedSockets = new LinkedList<Socket>();
    }

    public NodeDefinition getNodeDefinition() {
	return nodeDefinition;
    }

    public void connectToOtherNodes(Map<Integer, NodeDefinition> allNodes) {
	for (NodeDefinition nextDef : allNodes.values()) {
	    if (nextDef.getId() != nodeDefinition.getId()) {
		boolean connected = false;
		while (!connected) {
		    try {
			Socket socket = new Socket(nextDef.getIp(), nextDef.getPort());
			connectedSockets.add(socket);
			System.out.println("Verbindung hergestellt: " + nextDef);
			connected = true;
		    } catch (Exception e) {
			System.err.println("Verbindung mit port " + nextDef.getPort() + " fehlgeschlagen!");
		    }
		}
	    }
	}
    }

    @Override
    public void broadcastMessage() {
	for (Socket nextSocket : connectedSockets) {
	    OutputStreamWriter writer = null;
	    try {
		OutputStream out = nextSocket.getOutputStream();
		writer = new OutputStreamWriter(out);
		writer.write("Hallo, ich bin " + nodeDefinition);
	    } catch (Exception e) {
		System.err.println("Verbindung fehlgeschlagen!");
	    } finally {
		ResourceHelper.close(writer);
	    }
	}
    }

    @Override
    public void startServer() {
	try {
	    serverSocket = new ServerSocket(nodeDefinition.getPort());

	    while (true) {
		final Socket clientSocket = serverSocket.accept();
		System.out.print("Server has connected!\n");

		Thread thread = new SocketInputReader(clientSocket);
		thread.start();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.print("Whoops! It didn't work!\n");
	}
    }

    @Override
    public void closeServer() {
	if (serverSocket != null && !serverSocket.isClosed()) {
	    ResourceHelper.close(serverSocket);
	}
    }

    public void closeAllConnections() {
	try {
	    for (Socket socket : connectedSockets) {
		ResourceHelper.close(socket);
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	System.out.println("All connections closed");
    }

}
