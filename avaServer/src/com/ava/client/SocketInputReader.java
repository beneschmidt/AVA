package com.ava.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.ava.utils.ResourceHelper;

public class SocketInputReader extends Thread {
	private Socket socket;

	public SocketInputReader(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// TODO: endlos lesen bis Endzeichen kommt
			InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());

			while (inputReader.read() != -1) {
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ResourceHelper.close(socket);
		}
	}
}
