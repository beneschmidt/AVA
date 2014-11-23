package com.ava.advertisement;

import com.ava.socket.SocketMessage;

public class BoughtItem implements Comparable<BoughtItem> {

	private int count;
	private int max;

	private SocketMessage message;

	public BoughtItem(SocketMessage message, int max) {
		this.message = message;
		this.max = max;
		count = 1;
	}

	public void boughtAgain() throws IndexOutOfBoundsException {
		if (count < max) {
			count++;
		} else {
			throw new IndexOutOfBoundsException("already bought enough!");
		}
	}

	public SocketMessage getMessage() {
		return message;
	}

	public int getBoughtCount() {
		return count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMessage(SocketMessage message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object obj) {
		BoughtItem other = (BoughtItem) obj;
		return this.compareTo(other) == 0;
	}

	@Override
	public int compareTo(BoughtItem o) {
		// TODO maybe compare with initiator too
		//		int initiatorCompare = message.getInitiator().compareTo(o.getMessage().getInitiator());
		//		if (initiatorCompare == 0) {
		//			return message.getMessage().compareTo(o.getMessage().getMessage());
		//		} else {
		//			return initiatorCompare;
		//		}
		return message.getMessage().compareTo(o.getMessage().getMessage());
	}

}
