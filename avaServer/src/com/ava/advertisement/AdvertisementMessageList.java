package com.ava.advertisement;

import java.util.LinkedList;
import java.util.List;

import com.ava.socket.SocketMessage;

public class AdvertisementMessageList extends BusinessMessageList<AdvertisementMessage> {
	private List<AdvertisementMessage> messages;

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
	public AdvertisementMessage createNewObject(SocketMessage message) {
		return new AdvertisementMessage(message, getMax());
	}

	@Override
	public boolean shouldIAdvertiseFurtherMore() {
		return true;
	}

	@Override
	public List<AdvertisementMessage> getMessages() {
		return messages;
	}
}
