package com.ava.socket;

import java.util.Map;
import java.util.TreeMap;

import com.ava.Statistics;
import com.ava.node.NodeDefinition;
import com.ava.socket.SocketMessage.SocketMessageAction;

/**
 * statistics for rumors that were send over the network. It is not running in an own thread, but uses the SINGLETON pattern, so only one object is targetet
 * from other threads that may alter the information
 */
public class RumorStatistics implements Statistics {

	private static RumorStatistics instance;

	private Map<NodeDefinition, Boolean> rumorNodeMap;

	private RumorStatistics() {
		rumorNodeMap = new TreeMap<NodeDefinition, Boolean>();
	}

	public static RumorStatistics getInstance() {
		if (instance == null) {
			instance = new RumorStatistics();
		}
		return instance;
	}

	public void rumorCheckedAtNode(NodeDefinition node, Boolean nodeBelievedIt) {
		rumorNodeMap.put(node, nodeBelievedIt);
	}

	@Override
	public int checkedNodesCount() {
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

	@Override
	public String getFilePrefix() {
		return "rumorStatistics";
	}

	@Override
	public SocketMessageAction getMessageAction() {
		return SocketMessageAction.checkRumor;
	}

	@Override
	public void clear() {
		rumorNodeMap.clear();
	}
}
