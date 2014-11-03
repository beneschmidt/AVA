package com.ava.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.ava.utils.TimeUtils;

/**
 * Thread to read input from a socket
 * 
 * @author Benne
 */
public class SocketInputReader extends Thread {
    private static final String EXIT = "E";
    private Socket socket;

    public SocketInputReader(Socket socket) {
	this.socket = socket;
    }

    @Override
    public void run() {
	try {
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    String inputLine = "";
	    while ((inputLine = in.readLine()) != null) {
		String currentTime = TimeUtils.getCurrentTimestampString();
		System.out.println("[IN] " + currentTime + ": " + inputLine);
		if (inputLine.equals(EXIT)) {
		    System.out.println("Connection will be closed");
		    break;
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
