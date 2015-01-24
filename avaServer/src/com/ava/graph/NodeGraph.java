package com.ava.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ava.node.NodeDefinition;
import com.ava.node.NodeType;

/**
 * represents a complete node graph with all combinations based on a graph file
 * content
 */
public class NodeGraph {
	private static final String NODE_COMBINATION_PATTERN = "(\\d{1,2}) \\-\\- (\\d{1,2})";

	private List<GraphNodeCombination> combinations;
	private Map<Integer, NodeDefinition> allNodes;

	/**
	 * from file
	 * @param allNodes
	 * @param rows
	 */
	public NodeGraph(Map<Integer, NodeDefinition> allNodes, List<String> rows) {
		combinations = new LinkedList<>();
		this.allNodes = allNodes;
		// delete first and last row (graph definition rows)
		rows.remove(0);
		rows.remove(rows.size() - 1);

		combinations = loadCombinations(rows);
	}

	/**
	 * to file
	 * @param combinations
	 * @param allNodes
	 */
	public NodeGraph(List<GraphNodeCombination> combinations, Map<Integer, NodeDefinition> allNodes) {
		this.combinations = combinations;
		this.allNodes = allNodes;
	}

	private List<GraphNodeCombination> loadCombinations(List<String> rows) {
		for (String nextRow : rows) {
			Pattern pattern = Pattern.compile(NODE_COMBINATION_PATTERN);
			Matcher m = pattern.matcher(nextRow);
			if (m.find()) {
				Integer key = Integer.parseInt(m.group(1));
				Integer value = Integer.parseInt(m.group(2));
				combinations.add(new GraphNodeCombination(key, value));
			}
		}

		return combinations;
	}

	public List<GraphNodeCombination> getCombinations() {
		return combinations;
	}

	/**
	 * @param nodeId
	 * @return all mapped definitions for the given node id
	 */
	public List<NodeDefinition> getDefinitionsForNode(Integer nodeId) {
		List<NodeDefinition> definitions = new LinkedList<>();
		for (GraphNodeCombination nextCombination : combinations) {
			if (nextCombination.getKey().equals(nodeId)) {
				definitions.add(allNodes.get(nextCombination.getValue()));
			} else if (nextCombination.getValue().equals(nodeId)) {
				definitions.add(allNodes.get(nextCombination.getKey()));
			}
		}
		return definitions;
	}

	/**
	 * 
	 * @param nodeId
	 * @return all definitions except the already existing ones
	 */
	public List<NodeDefinition> getDefinitionsExceptForNode(Integer nodeId) {

		List<NodeDefinition> all = new LinkedList<>();
		for (NodeDefinition nextDef : allNodes.values()) {
			all.add(nextDef);
		}

		List<NodeDefinition> existing = getDefinitionsForNode(nodeId);

		for (NodeDefinition existingDef : existing) {
			all.remove(existingDef);
		}

		return all;
	}

	public void setCombinations(List<GraphNodeCombination> combinations) {
		this.combinations = combinations;
	}

	public void addCombination(GraphNodeCombination combination) {
		this.combinations.add(combination);
	}

	public Map<Integer, NodeDefinition> getAllNodes() {
		return allNodes;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder().append("graph G {\n");
		for (NodeDefinition def : allNodes.values()) {
			content.append("\"").append(def.getId()).append("\"").append(" [color=")
					.append(def.getNodeType() == NodeType.business ? "greenyellow" : "firebrick").append("];\n");
		}
		for (GraphNodeCombination nextComb : combinations) {
			content.append(nextComb.toString()).append("\n");
		}
		content.append("}");
		return content.toString();
	}
}
