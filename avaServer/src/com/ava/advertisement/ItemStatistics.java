package com.ava.advertisement;

import java.util.Map;
import java.util.TreeMap;

import com.ava.Statistics;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageAction;

/**
 * statistics for items that were bought. It is not running in an own thread, but uses the SINGLETON pattern, so only one object is targetet
 * from other threads that may alter the information
 */
public class ItemStatistics implements Statistics {
	private static ItemStatistics instance;

	private Map<NodeDefinition, Integer> itemNodeMap;

	private ItemStatistics() {
		itemNodeMap = new TreeMap<NodeDefinition, Integer>();
	}

	public static ItemStatistics getInstance() {
		if (instance == null) {
			instance = new ItemStatistics();
		}
		return instance;
	}

	public void itemsCheckedAtNode(NodeDefinition node, Integer count) {
		itemNodeMap.put(node, count);
	}

	@Override
	public int checkedNodesCount() {
		return itemNodeMap.size();
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (Map.Entry<NodeDefinition, Integer> entry : itemNodeMap.entrySet()) {
			string.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
		}
		return string.toString();
	}

	@Override
	public String getFilePrefix() {
		return "itemStatistics";
	}

	@Override
	public SocketMessageAction getMessageAction() {
		return SocketMessageAction.checkItemBought;
	}

	@Override
	public void clear() {
		itemNodeMap.clear();
	}
}
