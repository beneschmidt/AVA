package com.ava.advertisement;

import com.ava.socket.SocketMessage;

/**
 */
public abstract class BusinessMessage implements Comparable<BusinessMessage> {

	private SocketMessage message;
	private int heardOfCounter = 0;
	private int max;

	public BusinessMessage(SocketMessage message, int max) {
		this.message = message;
		this.max = max;
		newMessageArrived();
	}

	public void newMessageArrived() {
		heardOfCounter++;
	}

	public boolean shouldItBeBought() {
		return heardOfCounter >= max;
	}

	public int getHeardOfCounter() {
		return heardOfCounter;
	}

	public void setHeardOfCounter(int heardOfCounter) {
		this.heardOfCounter = heardOfCounter;
	}

	public SocketMessage getMessage() {
		return message;
	}

	public void setMessage(SocketMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BusinessMessage [message=" + message + ", heardOfCounter=" + heardOfCounter + ", max=" + max + "]";
	}

	/**
	 * natural order: type - nodeId - product
	 */
	@Override
	public int compareTo(BusinessMessage o) {
		int typeCompare = getMessage().getAction().compareTo(o.getMessage().getAction());
		if (typeCompare == 0) {
			int nodeCompare = getMessage().getNode().getId().compareTo(o.getMessage().getNode().getId());
			if (nodeCompare == 0) {
				return getMessage().getMessage().compareTo(o.getMessage().getMessage());
			} else {
				return nodeCompare;
			}
		} else {
			return typeCompare;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessMessage other = (BusinessMessage) obj;
		return compareTo(other) == 0;
	}

}
