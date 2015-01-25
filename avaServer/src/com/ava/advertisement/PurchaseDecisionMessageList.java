package com.ava.advertisement;

import java.util.LinkedList;
import java.util.List;

import com.ava.socket.SocketMessage;

public class PurchaseDecisionMessageList extends BusinessMessageList<PurchaseDecisionMessage> {

	List<PurchaseDecisionMessage> messages;
	private static PurchaseDecisionMessageList instance;

	private PurchaseDecisionMessageList() {
		messages = new LinkedList<>();
	}

	public static PurchaseDecisionMessageList getInstance() {
		if (instance == null) {
			instance = new PurchaseDecisionMessageList();
		}
		return instance;
	}

	@Override
	public PurchaseDecisionMessage createNewObject(SocketMessage message) {
		return new PurchaseDecisionMessage(message, getMax());
	}

	@Override
	public boolean shouldIAdvertiseFurtherMore() {
		return false;
	}

	@Override
	public List<PurchaseDecisionMessage> getMessages() {
		return messages;
	}

}
