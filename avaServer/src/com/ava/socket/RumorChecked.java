package com.ava.socket;

import java.util.Map;
import java.util.TreeMap;

import com.ava.node.NodeDefinition;

public class RumorChecked {

	private static RumorChecked instance;

	private Map<NodeDefinition, Boolean> checkState;

	private RumorChecked() {
		checkState = new TreeMap<NodeDefinition, Boolean>();
	}

	public static RumorChecked getInstance() {
		if (instance == null) {
			instance = new RumorChecked();
		}
		return instance;
	}

	public void addRumorCheck(NodeDefinition node, Boolean believed) {
		checkState.put(node, believed);
	}

	public int getCheckedNodesCount() {
		return checkState.size();
	}

	public Map<NodeDefinition, Boolean> getCheckState() {
		return checkState;
	}

	public void setCheckState(Map<NodeDefinition, Boolean> checkState) {
		this.checkState = checkState;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		int trueCount = 0;
		for (Map.Entry<NodeDefinition, Boolean> entry : checkState.entrySet()) {
			string.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			if (entry.getValue()) {
				trueCount++;
			}
		}
		string.append("\n").append(trueCount).append(" / ").append(checkState.size());
		return string.toString();
	}

}
