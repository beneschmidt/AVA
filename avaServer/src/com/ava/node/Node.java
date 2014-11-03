package com.ava.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.NodeServer;
import com.ava.socket.SocketInputReader;
import com.ava.socket.SocketOutputWriter;
import com.ava.utils.ResourceHelper;

public class Node implements NodeServer {

    private final NodeDefinition nodeDefinition;
    private Map<NodeDefinition, Socket> connectedSockets;
    private ServerSocket serverSocket;

    public Node(NodeDefinition nodeDefinition) {
	this.nodeDefinition = nodeDefinition;
	this.connectedSockets = new TreeMap<>();
    }

    public NodeDefinition getNodeDefinition() {
	return nodeDefinition;
    }

    public void connectToOtherNodes(List<NodeDefinition> nodesToConnect) {
	for (NodeDefinition nextDef : nodesToConnect) {
	    if (nextDef.getId() != nodeDefinition.getId()) {
		boolean connected = false;
		int connectionTries = 0;
		while (!connected && connectionTries < 10) {
		    try {
			connectionToNode(nextDef, connectionTries);
			connected = true;
		    } catch (Exception e) {
			System.err.println("Verbindung mit port " + nextDef.getPort() + " fehlgeschlagen!"+e.getMessage());
			connectionTries++;
			try {
			    Thread.sleep(1000);
			} catch (InterruptedException e1) {
			    e1.printStackTrace();
			}
		    }
		}
	    }
	}
    }

    private void connectionToNode(NodeDefinition nextDef, int connectionTries) throws UnknownHostException, IOException {
	Socket socket = new Socket(nextDef.getIp(), nextDef.getPort());
	connectedSockets.put(nextDef, socket);
	System.out.println("Verbindung hergestellt: " + nextDef);
    }

    @Override
    public void broadcastMessage(String message) {
	SocketOutputWriter writer = new SocketOutputWriter();
	for (Socket nextSocket : connectedSockets.values()) {
	    writer.writeMessage(nextSocket, message);
	}
    }

    public void message(NodeDefinition nodeToSendMessage, String message) {
	Socket socket = connectedSockets.get(nodeToSendMessage);
	SocketOutputWriter writer = new SocketOutputWriter();
	writer.writeMessage(socket, message);
    }

    public Map<NodeDefinition, Socket> getConnectedSockets() {
	return connectedSockets;
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
	    closeAllConnections();
	    ResourceHelper.close(serverSocket);
	}
    }

    public void closeAllConnections() {
	try {
	    SocketOutputWriter writer = new SocketOutputWriter();
	    for (Socket socket : connectedSockets.values()) {
		writer.writeMessage(socket, nodeDefinition + " goes offline");
		ResourceHelper.close(socket);
	    }
	    System.out.println("All connections closed");
	} catch (Exception e) {
	    System.out.println("Connections could not be closed properly. So sad.");
	}
    }

}
