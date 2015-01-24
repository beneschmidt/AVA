package com.ava.advertisement;

import java.util.LinkedList;
import java.util.List;

import com.ava.socket.SocketMessage;

/**
 * administrates all bought things
 * @author D063416
 *
 */
public class BoughtItems extends MaxValue {

	private static BoughtItems instance;

	private BoughtItems() {
		alreadyBought = new LinkedList<>();
	}

	private List<BoughtItem> alreadyBought;

	public static BoughtItems getInstance() {
		if (instance == null) {
			instance = new BoughtItems();
		}

		return instance;
	}

	public void itemBought(SocketMessage message) throws IndexOutOfBoundsException {
		BoughtItem item = new BoughtItem(message, getMax());
		BoughtItem foundItem = getItemIfExists(item);
		if (foundItem != null) {
			foundItem.boughtAgain();
		} else {
			alreadyBought.add(item);
		}
	}

	private BoughtItem getItemIfExists(BoughtItem item) {
		for (BoughtItem nextItem : alreadyBought) {
			if (nextItem.equals(item)) {
				return nextItem;
			}
		}
		return null;
	}

	public int buyCount(SocketMessage message) {
		BoughtItem item = new BoughtItem(message, getMax());
		BoughtItem foundItem = getItemIfExists(item);
		if (foundItem != null) {
			return foundItem.getBoughtCount();
		} else {
			return 0;
		}
	}

	public boolean canIBuyThat(SocketMessage message) {
		BoughtItem item = new BoughtItem(message, getMax());
		BoughtItem foundItem = getItemIfExists(item);
		if (foundItem != null && foundItem.getBoughtCount() >= getMax()) {
			//			System.out.println("I can't buy that anymore, I already bought it "+foundItem.getBoughtCount()+". times");
			return false;
		} else {
			// null oder noch nicht alle gekauft
			return true;
		}
	}
}
