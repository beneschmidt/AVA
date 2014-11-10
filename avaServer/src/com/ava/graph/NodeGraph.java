package com.ava.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ava.node.NodeDefinition;

/**
 * represents a complete node graph with all combinations based on a graph file
 * content
 */
public class NodeGraph {
	private static final String NODE_COMBINATION_PATTERN = "(\\d{1,2}) \\-\\- (\\d{1,2})";

	private List<GraphNodeCombination> combinations;
	private Map<Integer, NodeDefinition> allNodes;

	public NodeGraph(Map<Integer, NodeDefinition> allNodes, List<String> rows) {
		combinations = new LinkedList<>();
		this.allNodes = allNodes;
		// delete first and last row (graph definition rows)
		rows.remove(0);
		rows.remove(rows.size() - 1);

		combinations = loadCombinations(rows);
	}
	
	public NodeGraph(List<GraphNodeCombination> combinations){
		this.combinations=combinations;
	}

	private List<GraphNodeCombination> loadCombinations(List<String> rows) {
		for (String nextRow : rows) {
			Pattern pattern = Pattern.compile(NODE_COMBINATION_PATTERN);
			Matcher m = pattern.matcher(nextRow);
			if (m.find()) {
				Integer key = Integer.parseInt(m.group(1));
				Integer value = Integer.parseInt(m.group(2));
				combinations.add(new GraphNodeCombination(key, value));
			} else {
				throw new RuntimeException("Kann String leider nicht parsen!" + nextRow);
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
			}
		}
		return definitions;
	}

	public void setCombinations(List<GraphNodeCombination> combinations) {
		this.combinations = combinations;
	}
	
	@Override
	public String toString() {
		StringBuilder content = new StringBuilder()
		.append("graph G {\n");
		for(GraphNodeCombination nextComb : combinations){
			content.append(nextComb.toString()).append("\n");
		}
		content.append("}");
		return content.toString();
	}
}
