package com.ava.client;

public class ClientFactory {

	public static Client createClient(String host, int port){
		return new Client(host, port);
	}
}
