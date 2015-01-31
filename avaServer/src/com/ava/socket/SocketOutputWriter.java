package com.ava.socket;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ava.socket.SocketMessage.SocketMessageAction;

/**
 * helper class to write messages to a given socket using its outputstream
 */
public class SocketOutputWriter {

	private SocketMessageCounter messageCounter = SocketMessageCounter.getInstance();

	public boolean writeMessage(Socket socket, SocketMessage socketMessage) {
		OutputStreamWriter writer = null;
		try {
			OutputStream out = socket.getOutputStream();
			writer = new OutputStreamWriter(out);
			writer.write(socketMessage.asJson() + "\n");
			writer.flush();
			if (socketMessage.getAction() != SocketMessageAction.messageCountChecked) {
				messageCounter.sent();
			}
			return true;
		} catch (Exception e) {
			System.err.println("Verbindung fehlgeschlagen!" + e.getMessage());
			return false;
		}
	}
}
