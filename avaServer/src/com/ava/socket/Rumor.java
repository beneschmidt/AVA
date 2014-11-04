package com.ava.socket;

public class Rumor implements Comparable<Rumor> {

	private String message;
	private int receiveCount;

	public Rumor(String message) {
		receiveCount = 1;
		this.message = message;
	}

	public int iHeardThatToo() {
		receiveCount++;
		return receiveCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getReceiveCount() {
		return receiveCount;
	}

	@Override
	public String toString() {
		return "Rumors [message=" + message + ", receiveCount=" + receiveCount + "]";
	}

	@Override
	public int compareTo(Rumor o) {
		return message.compareTo(o.getMessage());
	}

}
