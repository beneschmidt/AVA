package com.ava.advertisement;

import java.util.Map;
import java.util.TreeMap;

import com.ava.node.NodeDefinition;

public class ItemStatistics {
	private static ItemStatistics instance;

	private Map<NodeDefinition, Integer> rumorNodeMap;

	private ItemStatistics() {
		rumorNodeMap = new TreeMap<NodeDefinition, Integer>();
	}

	public static ItemStatistics getInstance() {
		if (instance == null) {
			instance = new ItemStatistics();
		}
		return instance;
	}

	public void itemsCheckedAtNode(NodeDefinition node, Integer count) {
		rumorNodeMap.put(node, count);
	}

	public int checkedNodesCount() {
		return rumorNodeMap.size();
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (Map.Entry<NodeDefinition, Integer> entry : rumorNodeMap.entrySet()) {
			string.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
		}
		return string.toString();
	}
}
