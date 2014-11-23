package com.ava.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ava.node.NodeDefinition;
import com.ava.node.NodeListReader;
import com.ava.node.NodeType;
import com.ava.utils.FileWriterHelper;

/**
 * creates a graph file containing a graph representation
 * 
 * @author Benedikt Schmidt
 */
public class GraphCreator {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("usage: java -jar {jarname} {nodeFile} {kantenCount}");
			System.exit(0);
		}

		Map<Integer, NodeDefinition> nodes = NodeListReader.readNodeDefinitionsFromFile(args[0]);
		Integer nodeCount = nodes.size();
		Integer kantenCount = Integer.parseInt(args[1]);

		List<GraphNodeCombination> combinations = new LinkedList<>();

		while (combinations.size() < kantenCount) {
			Integer key = new Random().nextInt(nodeCount) + 1;
			Integer value = new Random().nextInt(nodeCount) + 1;
			GraphNodeCombination nextComb = new GraphNodeCombination(key, value);
			if (key != value && !combinations.contains(nextComb) && nodes.get(value).getNodeType()==NodeType.customer) {
				combinations.add(nextComb);
			}
		}

		NodeGraph graph = new NodeGraph(combinations);
		FileWriterHelper helper = new FileWriterHelper("graph.gv");
		helper.writeToFile(graph.toString());
	}
	/*
	 * 1-1
	 * 1-2
	 * 1-3
	 * 2-1
	 * 2-2
	 * 2-3
	 * 3-1
	 * 3-2
	 * 3-3
	 * (node * node) - node = max
	 */
}
