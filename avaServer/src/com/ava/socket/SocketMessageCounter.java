package com.ava.socket;

public class SocketMessageCounter {

	private static SocketMessageCounter instance;

	private SocketMessageCounter() {

	}

	public static synchronized SocketMessageCounter getInstance() {
		if (instance == null) {
			instance = new SocketMessageCounter();
		}

		return instance;
	}

	private int sendMessages;
	private int receivedMessages;

	public int getSendMessages() {
		return sendMessages;
	}

	public void sent() {
		this.sendMessages++;
	}

	public int getReceivedMessages() {
		return receivedMessages;
	}

	public void received() {
		this.receivedMessages++;
	}

	@Override
	public String toString() {
		return "SocketMessageCounter [sendMessages=" + sendMessages + ", receivedMessages=" + receivedMessages + "]";
	}

}
