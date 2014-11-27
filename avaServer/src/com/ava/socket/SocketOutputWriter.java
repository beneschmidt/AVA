package com.ava.socket;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * helper class to write messages to a given socket using its outputstream
 */
public class SocketOutputWriter {

	public boolean writeMessage(Socket socket, SocketMessage socketMessage) {
		OutputStreamWriter writer = null;
		try {
			OutputStream out = socket.getOutputStream();
			writer = new OutputStreamWriter(out);
			writer.write(socketMessage.asJson() + "\n");
			writer.flush();
			return true;
			//			System.out.println("\n[-->] " + TimeUtils.getCurrentTimeString() + ": " + socketMessage.getMessage());
		} catch (Exception e) {
			System.err.println("Verbindung fehlgeschlagen!" + e.getMessage());
			return false;
		}
	}
}
