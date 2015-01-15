package com.ava.utils;

import java.util.LinkedList;
import java.util.List;

public class DoubleCountHelper {

	public static final int maxCount = 2;

	public static class Count {
		private int send;
		private int received;

		public Count(int send, int received) {
			this.send = send;
			this.received = received;
		}

		public int getSend() {
			return send;
		}

		public void setSend(int send) {
			this.send = send;
		}

		public int getReceived() {
			return received;
		}

		public void setReceived(int received) {
			this.received = received;
		}

		@Override
		public String toString() {
			return "Count [send=" + send + ", received=" + received + "]";
		}
	}

	public List<Count> counts;

	public DoubleCountHelper() {
		counts = new LinkedList<>();
	}

	public void addCount(Count count) {
		if (counts.size() == 2) {
			counts.remove(0);
		}
		counts.add(count);
	}

	public boolean allEqual() {
		int[] allValues = new int[maxCount * 2];
		int i = 0;
		for (Count nextCount : counts) {
			allValues[i++] = nextCount.send;
			allValues[i++] = nextCount.received;
		}
		return ValueHelper.allEqual(allValues);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Count nextCount : counts) {
			s.append(nextCount).append(",");
		}
		return s.toString();
	}

}
