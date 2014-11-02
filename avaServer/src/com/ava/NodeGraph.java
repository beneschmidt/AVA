package com.ava;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ava.ueb01.NodeDefinition;

/**
 * represents a complete node graph with all combinations based on a graph file
 * content
 * 
 * @author Benne
 * 
 */
public class NodeGraph {
    private static final String NODE_COMBINATION_PATTERN = "(\\d{1,2}) \\-\\- (\\d{1,2})";

    private List<NodeCombination> combinations;
    private Map<Integer, NodeDefinition> allNodes;

    public NodeGraph(Map<Integer, NodeDefinition> allNodes, List<String> rows) {
	combinations = new LinkedList<>();
	this.allNodes = allNodes;
	// delete first and last row (graph definition rows)
	rows.remove(0);
	rows.remove(rows.size() - 1);

	combinations = loadCombinations(rows);
    }

    private List<NodeCombination> loadCombinations(List<String> rows) {
	for (String nextRow : rows) {
	    Pattern pattern = Pattern.compile(NODE_COMBINATION_PATTERN);
	    Matcher m = pattern.matcher(nextRow);
	    if (m.find()) {
		Integer key = Integer.parseInt(m.group(1));
		Integer value = Integer.parseInt(m.group(2));
		combinations.add(new NodeCombination(key, value));
	    } else {
		throw new RuntimeException("Kann String leider nicht parsen!" + nextRow);
	    }
	}

	return combinations;
    }

    public List<NodeCombination> getCombinations() {
	return combinations;
    }

    /**
     * @param nodeId
     * @return all mapped definitions for the given node id
     */
    public List<NodeDefinition> getDefinitionsForNode(Integer nodeId) {
	List<NodeDefinition> definitions = new LinkedList<>();
	for (NodeCombination nextCombination : combinations) {
	    if (nextCombination.getKey().equals(nodeId)) {
		definitions.add(allNodes.get(nextCombination.getValue()));
	    }
	}
	return definitions;
    }

    public void setCombinations(List<NodeCombination> combinations) {
	this.combinations = combinations;
    }

    public static class NodeCombination {
	private final Integer key;
	private final Integer value;

	public NodeCombination(Integer key, Integer value) {
	    this.key = key;
	    this.value = value;
	}

	public Integer getKey() {
	    return key;
	}

	public Integer getValue() {
	    return value;
	}

	@Override
	public String toString() {
	    return "NodeCombination [key=" + key + ", value=" + value + "]";
	}

    }
}
