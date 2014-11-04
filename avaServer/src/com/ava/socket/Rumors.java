package com.ava.socket;

import java.util.Map;
import java.util.TreeMap;

import com.ava.node.NodeDefinition;

/**
 * Singleton to administrate all rumors that are around
 * @author D063416
 */
public class Rumors {

	private static Rumors rumors;

	private Map<String, Rumor> rumorList;

	private Rumors() {
		rumorList = new TreeMap<>();
	}

	public static Rumors getInstance() {
		if (rumors == null) {
			rumors = new Rumors();
		}

		return rumors;
	}

	/**
	 * @param message
	 * @return receive count
	 */
	public int addRumor(NodeDefinition node, String message) {
		if (rumorList.get(message) == null) {
			Rumor rumor = new Rumor(message);
			rumorList.put(message, rumor);
			return rumor.getReceiveCount();
		} else {
			int count = rumorList.get(message).iHeardThatToo(node);
			return count;
		}
	}
}
