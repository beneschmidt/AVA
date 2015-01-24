package com.ava.graph;

/**
 * combination of a node and his neighbour in a graph
 */
public class GraphNodeCombination {
	private final Integer key;
	private final Integer value;

	public GraphNodeCombination(Integer key, Integer value) {
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
		return key + " -- " + value + ";";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNodeCombination other = (GraphNodeCombination) obj;
		if (key == other.key && value == other.value) {
			return true;
		}
		if (key == other.value && value == other.key) {
			return true;
		}
		return false;
	}

}