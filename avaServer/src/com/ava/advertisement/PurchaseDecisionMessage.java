package com.ava.advertisement;

import com.ava.socket.SocketMessage;

public class PurchaseDecisionMessage extends BusinessMessage {

	public PurchaseDecisionMessage(SocketMessage message, int max) {
		super(message, max);
	}

}
