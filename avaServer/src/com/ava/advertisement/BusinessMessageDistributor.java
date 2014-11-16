package com.ava.advertisement;

import com.ava.socket.SocketMessage;

public class BusinessMessageDistributor {

	public static boolean distribute(SocketMessage message){
		switch(message.getAction()){
			case advertisement: {
				return AdvertisementMessageList.getInstance().iHeardThatAndIWonderedIfIShouldBuy(message);
			}
			case purchaseDecision: {
				return PurchaseDecisionMessageList.getInstance().iHeardThatAndIWonderedIfIShouldBuy(message);
			}
			default: {
				throw new RuntimeException("das sollte nicht hier angekommen sein!");
			}
		}
	}
}
