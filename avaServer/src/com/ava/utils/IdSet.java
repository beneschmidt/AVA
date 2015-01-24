package com.ava.utils;

import java.util.LinkedList;
import java.util.List;

public class IdSet {

	private List<Integer> ids;

	public IdSet() {
		ids = new LinkedList<Integer>();
	}

	public void loadFromString(String idString) {
		for (String nextString : idString.split(",")) {
			Integer newId = Integer.parseInt(nextString);
			addId(newId);
		}
	}

	public void addId(Integer id) {
		if (!ids.contains(id)) {
			ids.add(id);
		}
	}

	public List<Integer> getValues() {
		return ids;
	}

	public int size() {
		return ids.size();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Integer id : ids) {
			s.append(id).append(",");
		}
		if (s.length() > 0) {
			s.deleteCharAt(s.lastIndexOf(","));
		}
		return s.toString();
	}

}
