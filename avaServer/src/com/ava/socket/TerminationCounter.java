package com.ava.socket;

public class TerminationCounter {
	private static TerminationCounter instance;

	private TerminationCounter() {

	}

	public static TerminationCounter getInstance() {
		if (instance == null) {
			instance = new TerminationCounter();
		}

		return instance;
	}

	private int sendMessages = 0;
	private int receivedMessages = 0;

	public int getSendMessages() {
		return sendMessages;
	}

	public void addSendMessages(int sendMessages) {
		this.sendMessages += sendMessages;
	}

	public int getReceivedMessages() {
		return receivedMessages;
	}

	public void addReceivedMessages(int receivedMessages) {
		this.receivedMessages += receivedMessages;
	}

	public void clear() {
		sendMessages = 0;
		receivedMessages = 0;
	}

	@Override
	public String toString() {
		return "TerminationCounter [sendMessages=" + getSendMessages() + ", receivedMessages=" + getReceivedMessages() + "]";
	}

}
