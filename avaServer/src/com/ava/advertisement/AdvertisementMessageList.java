package com.ava.advertisement;

import java.util.LinkedList;

import com.ava.socket.SocketMessage;

public class AdvertisementMessageList extends BusinessMessageList<AdvertisementMessage> {

	private static AdvertisementMessageList instance;

	private AdvertisementMessageList() {
		messages = new LinkedList<>();
	}

	public static AdvertisementMessageList getInstance() {
		if (instance == null) {
			instance = new AdvertisementMessageList();
		}

		return instance;
	}

	@Override
	public boolean iHeardThatAndIWonderedIfIShouldBuy(SocketMessage socketMessage) {
		AdvertisementMessage newMessage = new AdvertisementMessage(socketMessage, getMax());
		if (messages.contains(newMessage)) {
			// walk through all the messages to find out which one should be updated
			for (AdvertisementMessage nextMessage : messages) {
				if (nextMessage.equals(newMessage)) {
					nextMessage.newMessageArrived();
					return nextMessage.shouldItBeBought();
				}
			}
		} else {
			messages.add(newMessage);
			return newMessage.shouldItBeBought();
		}
		return false;
	}

	@Override
	public AdvertisementMessage createNewObject(SocketMessage message) {
		return new AdvertisementMessage(message, getMax());
	}
}
