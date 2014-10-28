package com.ava.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private final int port;

	public Server(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		String data = "Toobie ornaught toobie";
		System.out.println("Los gehts!");
		try {
			ServerSocket srvr = new ServerSocket(port);
			Socket skt = srvr.accept();
			System.out.print("Server has connected!\n");
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
			System.out.print("Sending string: '" + data + "'\n");
			out.print(data);
			out.close();
			skt.close();
			srvr.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Whoops! It didn't work!\n");
		}
	}

	@Override
	public String toString() {
		return "Server [port=" + port + "]";
	}
	
	public static void main(String[] args) {
		Server server = new Server(1234);
		server.start();
	}
}
