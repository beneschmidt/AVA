package com.ava.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.socket.SocketInputReader;
import com.ava.socket.SocketMessage;
import com.ava.socket.SocketMessage.SocketMessageAction;
import com.ava.socket.SocketMessageFactory;
import com.ava.socket.SocketOutputWriter;
import com.ava.utils.ResourceHelper;

public class Node implements NodeServer {

	private final NodeDefinition nodeDefinition;
	private volatile Map<NodeDefinition, Socket> connectedSockets;
	protected Map<Integer, NodeDefinition> nodes;
	private ServerSocket serverSocket;
	private EchoStatus echoStatus;
	protected SocketOutputWriter writer = new SocketOutputWriter();

	public Node(NodeDefinition nodeDefinition, Map<Integer, NodeDefinition> nodes) {
		this.nodeDefinition = nodeDefinition;
		this.connectedSockets = new TreeMap<>();
		echoStatus = new EchoStatus();
		this.nodes = nodes;
	}

	public NodeDefinition getNodeDefinition() {
		return nodeDefinition;
	}

	public void connectToOtherNodes(Collection<NodeDefinition> nodesToConnect) {
		for (NodeDefinition nextDef : nodesToConnect) {
			if (nextDef.getId() != nodeDefinition.getId()) {
				boolean connected = false;
				int connectionTries = 0;
				while (!connected && connectionTries < 10) {
					try {
						connectionToNode(nextDef);
						connected = true;
					} catch (Exception e) {
						System.err.println("Verbindung mit port " + nextDef.getPort() + " fehlgeschlagen!" + e.getMessage());
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

	public List<Integer> getConnectedIds() {
		List<Integer> ids = new LinkedList<>();
		for (NodeDefinition def : getConnectedSockets().keySet()) {
			ids.add(def.getId());
		}
		return ids;
	}

	public void connectionToNode(NodeDefinition nextDef) throws UnknownHostException, IOException {
		Socket socket = new Socket(nextDef.getIp(), nextDef.getPort());
		connectedSockets.put(nextDef, socket);
		System.out.println("Verbindung hergestellt: " + nextDef);
	}

	@Override
	public int broadcastMessage(SocketMessage message) {
		return this.sendMessage(connectedSockets, message);
	}

	public int sendMessage(Map<NodeDefinition, Socket> sockets, SocketMessage message) {
		int sendCount = 0;
		synchronized (sockets) {
			for (Socket nextSocket : sockets.values()) {
				boolean successful = writer.writeMessage(nextSocket, message);
				if (successful) {
					sendCount++;
				}
			}
		}
		return sendCount;
	}

	@Override
	public boolean sendMessage(NodeDefinition nodeToSendMessage, SocketMessage message) {
		Socket socket = connectedSockets.get(nodeToSendMessage);
		return writer.writeMessage(socket, message);
	}

	public boolean sendSingleMessage(NodeDefinition n, SocketMessage message) {
		Socket socket = null;
		try {
			socket = new Socket(n.getIp(), n.getPort());
			return writer.writeMessage(socket, message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ResourceHelper.close(socket);
		}
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
						Thread thread = new SocketInputReader(Node.this, clientSocket);
						thread.start();
					}
				} catch (Exception e) {
					System.out.print(e.getMessage());
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
			SocketMessage socketMessage = SocketMessageFactory.createSystemMessage(nodeDefinition, nodeDefinition, nodeDefinition + " goes offline",
					SocketMessageAction.closed);
			SocketOutputWriter writer = new SocketOutputWriter();
			for (Socket socket : connectedSockets.values()) {
				writer.writeMessage(socket, socketMessage);
				ResourceHelper.close(socket);
			}
			System.out.println("All connections closed");
		} catch (Exception e) {
			System.out.println("Connections could not be closed properly. So sad.");
		}
	}

	public EchoStatus getEchoStatus() {
		return echoStatus;
	}

	public void setEchoStatus(EchoStatus echoStatus) {
		this.echoStatus = echoStatus;
	}

}
