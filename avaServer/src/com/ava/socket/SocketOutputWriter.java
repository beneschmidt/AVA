package com.ava.socket;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ava.utils.TimeUtils;

/**
 * helper class to write messages to a given socket using its outputstream
 */
public class SocketOutputWriter {

	public void writeMessage(Socket socket, SocketMessage socketMessage) {
		OutputStreamWriter writer = null;
		try {
			OutputStream out = socket.getOutputStream();
			writer = new OutputStreamWriter(out);
			writer.write(socketMessage.asJson() + "\n");
			writer.flush();
			System.out.println("\n[-->] " + TimeUtils.getCurrentTimeString() + ": " + socketMessage.getMessage());
		} catch (Exception e) {
			System.err.println("Verbindung fehlgeschlagen!" + e.getMessage());
		}
	}
}
