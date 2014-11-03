package com.ava.graph;

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
		return key +" -- "+value+";";
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
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}