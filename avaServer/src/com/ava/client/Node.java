package com.ava.client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

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

    public void connectToOtherNodes(List<NodeDefinition> nodesToConnect) {
	for (NodeDefinition nextDef : nodesToConnect) {
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
	SocketOutputWriter writer = new SocketOutputWriter();
	for (Socket nextSocket : connectedSockets) {
	    writer.writeMessage(nextSocket, "Hey, I'm " + nodeDefinition);
	}
    }

    @Override
    public void startServerAsThread() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    System.out.println("Server Socket gets start!");
		    serverSocket = new ServerSocket(nodeDefinition.getPort());

		    while (true) {
			final Socket clientSocket = serverSocket.accept();
			Thread thread = new SocketInputReader(clientSocket);
			thread.start();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.print("Whoops! It didn't work!\n");
		}
	    }
	}).start();
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
	    System.out.println("All connections closed");
	} catch (Exception e) {
	    System.out.println("Connections could not be closed properly. So sad.");
	}
    }

}
