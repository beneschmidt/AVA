package com.ava.advertisement;

import java.util.List;

import com.ava.socket.SocketMessage;

public abstract class BusinessMessageList<T extends BusinessMessage> extends MaxValue {

	private static Object lockObject = new Object();

	public boolean iHeardThatAndIWonderedIfIShouldBuy(SocketMessage socketMessage) {
		T newMessage = createNewObject(socketMessage);
		if (getMessages().contains(newMessage)) {
			// walk through all the messages to find out which one should be updated
			for (T nextMessage : getMessages()) {
				if (nextMessage.equals(newMessage)) {
					nextMessage.newMessageArrived();
					return nextMessage.shouldItBeBought();
				}
			}
		} else {
			getMessages().add(newMessage);
			return newMessage.shouldItBeBought();
		}
		return false;
	}

	public void clearHistoryForMessage(SocketMessage message) {
		T object = createNewObject(message);
		int index = findIndexOfAd(object);
		if (index != -1) {
			getMessages().remove(index);
		}
	}

	private int findIndexOfAd(T message) {
		int i = 0;
		for (T nextMessage : getMessages()) {
			if (nextMessage.equals(message)) {
				return i;
			} else {
				i++;
			}
		}
		return -1;
	}

	public int currentCount(SocketMessage message) {
		T object = createNewObject(message);
		return getMessages().get(findIndexOfAd(object)).getHeardOfCounter();
	}

	public abstract List<T> getMessages();

	public abstract T createNewObject(SocketMessage message);

	public abstract boolean shouldIAdvertiseFurtherMore();

	public Object getLockObject() {
		return lockObject;
	}

}
