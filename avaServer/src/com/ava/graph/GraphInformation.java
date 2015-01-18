package com.ava.graph;


/**
 * keeps information of the graph in a class to reach all over the current node that is running
 */
public class GraphInformation {

	private static GraphInformation instance;
	private NodeGraph fullGraph;

	private GraphInformation() {

	}

	public static GraphInformation getInstance() {
		if (instance == null) {
			instance = new GraphInformation();
		}
		return instance;
	}

	public NodeGraph getFullGraph() {
		return fullGraph;
	}

	public void setFullGraph(NodeGraph fullGraph) {
		this.fullGraph = fullGraph;
	}

}
