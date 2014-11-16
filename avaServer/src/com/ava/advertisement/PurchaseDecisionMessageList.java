package com.ava.advertisement;

import java.util.LinkedList;
import java.util.List;

import com.ava.socket.SocketMessage;

public class PurchaseDecisionMessageList implements BusinessMessageList {

	private static PurchaseDecisionMessageList instance;
	private List<PurchaseDecisionMessage> messages;
	private static final int max = 3;

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
	public boolean iHeardThatAndIWonderedIfIShouldBuy(SocketMessage socketMessage) {
		PurchaseDecisionMessage newMessage = new PurchaseDecisionMessage(socketMessage, max);
		if (messages.contains(newMessage)) {
			// walk through all the messages to find out which one should be updated
			for (PurchaseDecisionMessage nextMessage : messages) {
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
