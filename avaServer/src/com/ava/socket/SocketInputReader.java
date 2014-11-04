package com.ava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.ava.node.Node;
import com.ava.utils.TimeUtils;

/**
 * Thread to read input from a socket
 * 
 * @author Benne
 */
public class SocketInputReader extends Thread {
	private static final String EXIT = "EXIT";
	
	private Socket socket;
	private Node node;

	public SocketInputReader(Node node, Socket socket) {
		this.socket = socket;
		this.node = node;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				String currentTime = TimeUtils.getCurrentTimestampString();
				SocketMessage message = SocketMessage.fromJson(inputLine);
				System.out.println("[IN] " + currentTime + ": " + message.getMessage());
				
				handleMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessage(SocketMessage message) {
		if(message.getMessage().equals(EXIT)){
			node.closeServer();
		}
	}
}
