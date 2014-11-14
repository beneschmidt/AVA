package com.ava.node;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.utils.FileReaderHelper;

public class NodeListReader {

	/**
	 * @param filename
	 * @return Map with node definitions
	 */
	public static Map<Integer, NodeDefinition> readNodeDefinitionsFromFile(String filename) {
		FileReaderHelper fileReaderHelper = new FileReaderHelper(filename);
		Map<Integer, NodeDefinition> nodes = new TreeMap<Integer, NodeDefinition>();
		List<String> lines = fileReaderHelper.readFileAsRows();
		for (String nextLine : lines) {
			NodeDefinition nodeDefinition = new NodeDefinition(nextLine);
			nodes.put(nodeDefinition.getId(), nodeDefinition);
			System.out.println(nextLine);
		}
		System.out.println("Insgesamt gefunden: " + nodes.size());
		return nodes;
	}
}
