package com.ava.socket;

import java.util.Map;
import java.util.TreeMap;

import com.ava.node.NodeDefinition;

/**
 * Singleton to keep all nodes, that were checked for a specific rumor
 */
public class RumorChecker {

	private static RumorChecker instance;

	private Map<NodeDefinition, Boolean> rumorNodeMap;

	private RumorChecker() {
		rumorNodeMap = new TreeMap<NodeDefinition, Boolean>();
	}

	public static RumorChecker getInstance() {
		if (instance == null) {
			instance = new RumorChecker();
		}
		return instance;
	}

	public void rumorCheckedAtNode(NodeDefinition node, Boolean nodeBelievedIt) {
		rumorNodeMap.put(node, nodeBelievedIt);
	}

	public int getCheckedNodesCount() {
		return rumorNodeMap.size();
	}

	public Map<NodeDefinition, Boolean> getCheckState() {
		return rumorNodeMap;
	}

	public void setCheckState(Map<NodeDefinition, Boolean> checkState) {
		this.rumorNodeMap = checkState;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		int trueCount = 0;
		for (Map.Entry<NodeDefinition, Boolean> entry : rumorNodeMap.entrySet()) {
			string.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			if (entry.getValue()) {
				trueCount++;
			}
		}
		string.append("\n").append(trueCount).append(" / ").append(rumorNodeMap.size());
		return string.toString();
	}

}
