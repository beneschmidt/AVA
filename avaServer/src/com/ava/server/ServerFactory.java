package com.ava.server;

public class ServerFactory {

	public static Server createServer(int port){
		return new Server(port);
	}
}
