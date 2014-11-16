package com.ava.advertisement;

import java.util.LinkedList;
import java.util.List;

import com.ava.socket.SocketMessage;

public class AdvertisementMessageList implements BusinessMessageList{

	private static AdvertisementMessageList instance;
	private static final int max = 3;

	private List<AdvertisementMessage> messages;

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
		AdvertisementMessage newMessage = new AdvertisementMessage(socketMessage, max);
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

}
