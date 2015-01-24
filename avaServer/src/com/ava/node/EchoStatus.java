package com.ava.node;

import com.ava.utils.IdSet;

public class EchoStatus {

	public enum EchoColor {
		white, red, green;
	}

	private EchoColor color;
	private NodeDefinition firstNeighbour;
	private int count;
	private boolean isInitiator;
	private IdSet ids;
	private long startingTime;
	private long finishTime;

	public EchoStatus() {
		clear();
	}

	public void clear() {
		color = EchoColor.white;
		count = 0;
		isInitiator = false;
		ids = new IdSet();
	}

	public EchoColor getColor() {
		return color;
	}

	public void setColor(EchoColor color) {
		this.color = color;
	}

	public NodeDefinition getFirstNeighbour() {
		return firstNeighbour;
	}

	public void setFirstNeighbour(NodeDefinition firstNeighbour) {
		this.firstNeighbour = firstNeighbour;
	}

	public synchronized int getCount() {
		return count;
	}

	public synchronized void increaseCount() {
		count++;
	}

	public boolean isInitiator() {
		return isInitiator;
	}

	public void initiated() {
		this.isInitiator = true;
		this.startingTime = System.currentTimeMillis();
	}

	public void stopped() {
		this.finishTime = System.currentTimeMillis();
	}

	public IdSet getIds() {
		return ids;
	}

	public void setIds(IdSet ids) {
		this.ids = ids;
	}

	public void loadIds(String idString) {
		ids.loadFromString(idString);
	}

	public void addId(Integer id) {
		ids.addId(id);
	}

	public long getStartingTime() {
		return startingTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public void setStartingTime(long startingTime) {
		this.startingTime = startingTime;
	}

	@Override
	public String toString() {
		return "EchoStatus [color=" + color + ", firstNeighbour=" + firstNeighbour + ", count=" + count + ", isInitiator=" + isInitiator + "]";
	}

	public long timeInMilliseconds() {
		return System.currentTimeMillis() - startingTime;
	}

}
