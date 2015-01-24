package com.ava.utils;

import java.util.LinkedHashSet;
import java.util.Set;

public class IdSet {

	private Set<Integer> ids;

	public IdSet() {
		ids = new LinkedHashSet<Integer>();
	}

	public void loadFromString(String idString) {
		for (String nextString : idString.split(",")) {
			Integer newId = Integer.parseInt(nextString);
			ids.add(newId);
		}
	}

	public void addId(Integer id) {
		ids.add(id);
	}

	public Set<Integer> getValues() {
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
