package com.ava.advertisement;

import java.util.List;

import com.ava.socket.SocketMessage;

public abstract class BusinessMessageList<T extends BusinessMessage> {
	protected List<T> messages;

	public abstract boolean iHeardThatAndIWonderedIfIShouldBuy(SocketMessage socketMessage);

	public void clearHistoryForMessage(SocketMessage message) {
		int index = findIndexOfAd(message);
		messages.remove(index);
	}

	private int findIndexOfAd(SocketMessage message) {
		int i = 0;
		for (T nextMessage : messages) {
			if (nextMessage.equals(message)) {
				return i;
			} else {
				i++;
			}
		}
		return -1;
	}

	public abstract T createNewObject(SocketMessage message);
}
