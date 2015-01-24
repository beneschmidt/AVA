package com.ava.utils;

import java.util.Set;
import java.util.TreeSet;

import com.ava.graph.GraphInformation;
import com.ava.node.EchoStatus;

/**
 * Analysis class, that analyses a given echoStatus
 */
public class EchoAnalysis {

	private EchoStatus echoStatus;

	public EchoAnalysis(EchoStatus echoStatus) {
		this.echoStatus = echoStatus;
	}

	public long getTimeTaken() {
		return echoStatus.getFinishTime() - echoStatus.getStartingTime();
	}

	public long getReachedNodeCount() {
		return echoStatus.getIds().size();
	}

	public Set<Integer> getReachedNodes() {
		return echoStatus.getIds().getValues();
	}

	public long getAllNodesCount() {
		return GraphInformation.getInstance().getFullGraph().getAllNodes().size();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder().append("EchoAnalysis...\n [time=").append(getTimeTaken()).append("ms, number of reached nodes=")
				.append(getReachedNodeCount()).append("/").append(getAllNodesCount() + "\n").append("Unsorted Reached nodes: ").append(getReachedNodes())
				.append("\n").append("Sorted Reached nodes: ").append(getReachedNodesSorted()).append("\n").append("Not reached nodes: ")
				.append(getNotReachedNodes()).append("]");
		return s.toString();
	}

	private Object getReachedNodesSorted() {
		return new TreeSet<Integer>(getReachedNodes());
	}

	private Object getNotReachedNodes() {
		Set<Integer> notReached = new TreeSet<>();
		for (Integer id : GraphInformation.getInstance().getFullGraph().getAllNodes().keySet()) {
			if (!getReachedNodes().contains(id)) {
				notReached.add(id);
			}
		}
		return notReached;
	}
}
