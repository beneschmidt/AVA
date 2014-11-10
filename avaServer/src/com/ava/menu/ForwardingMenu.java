package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.ava.socket.SocketMessage.SocketMessageForwardingType;

/**
 * menu to select the forwarding type
 */
public class ForwardingMenu implements Menu {
	private static final int BROADCAST_TO_HALF = 5;
	private static final int BROADCAST_BUT_TWO = 4;
	private static final int BROADCAST_WITHOUT_RECEIVER = 3;
	private static final int BROADCAST = 2;
	private static final int NONE = 1;
	private static Map<Integer, String> selections;
	static {
		selections = new TreeMap<>();
		selections.put(NONE, "no forwarding");
		selections.put(BROADCAST, "forward to every known node");
		selections.put(BROADCAST_WITHOUT_RECEIVER, "forward to every known node but me");
		selections.put(BROADCAST_BUT_TWO, "forward to every known node but me and two others");
		selections.put(BROADCAST_TO_HALF, "forward to half of known nodes (not me)");
	}

	@Override
	public Object run() {
		System.out.println(toString());
		Object o = readInput();
		return handleInput(o);
	}

	private SocketMessageForwardingType handleInput(Object o) {
		try {
			Integer idToHandle = Integer.valueOf(o.toString());
			switch (idToHandle) {
				case NONE: {
					return SocketMessageForwardingType.none;
				}
				case BROADCAST: {
					return SocketMessageForwardingType.broadcast;
				}
				case BROADCAST_WITHOUT_RECEIVER: {
					return SocketMessageForwardingType.broadcast_without_sender;
				}
				case BROADCAST_BUT_TWO: {
					return SocketMessageForwardingType.broadcast_to_all_but_two;
				}
				case BROADCAST_TO_HALF: {
					return SocketMessageForwardingType.broadcast_to_half;
				}
				default: {
					return SocketMessageForwardingType.none;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return SocketMessageForwardingType.none;
		}

	}

	private Integer readInput() {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter selection:");
			try {
				String inputLine = br.readLine();
				Integer id = Integer.parseInt(inputLine);
				if (selections.containsKey(id)) {
					return id;
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder menu = new StringBuilder().append("What do you want to do?\n");
		for (Map.Entry<Integer, String> entries : selections.entrySet()) {
			menu.append(entries.getKey()).append(") ").append(entries.getValue()).append("\n");
		}
		return menu.toString();
	}
}
