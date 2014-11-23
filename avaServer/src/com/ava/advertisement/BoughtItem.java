package com.ava.advertisement;

import com.ava.socket.SocketMessage;

public class BoughtItem implements Comparable<BoughtItem> {

	private int count;

	private SocketMessage message;

	public BoughtItem(SocketMessage message) {
		this.message = message;
		count = 1;
	}

	public void boughtAgain() {
		count++;
	}

	public SocketMessage getMessage() {
		return message;
	}

	public int getBoughtCount() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoughtItem other = (BoughtItem) obj;
		return this.compareTo(other) == 0;
	}

	@Override
	public int compareTo(BoughtItem o) {
		int initiatorCompare = message.getInitiator().compareTo(o.getMessage().getInitiator());
		if (initiatorCompare == 0) {
			return message.getMessage().compareTo(o.getMessage().getMessage());
		} else {
			return initiatorCompare;
		}
	}

}
