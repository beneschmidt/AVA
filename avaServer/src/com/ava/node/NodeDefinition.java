package com.ava.node;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * node definition including an id, ip and port
 */
public class NodeDefinition implements Comparable<NodeDefinition> {

	private final Integer id;
	private final String ip;
	private final int port;

	/**
	 * Node definition can be created through a given string
	 * @param stringDefinition
	 */
	public NodeDefinition(String stringDefinition) {
		List<String> values = parseDefinitionByString(stringDefinition);
		id = Integer.parseInt(values.get(0));
		ip = values.get(1);
		port = Integer.parseInt(values.get(2));
	}

	private List<String> parseDefinitionByString(String stringDefinition) {
		String pattern = "(\\d{0,2}) (.*):(\\d{1,6})";
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);
		List<String> values = new LinkedList<String>();

		// Now create matcher object.
		Matcher m = r.matcher(stringDefinition);
		if (m.find()) {
			values.add(m.group(1));
			values.add(m.group(2));
			values.add(m.group(3));
			return values;
		} else {
			throw new RuntimeException("Kann String leider nicht parsen!" + stringDefinition);
		}
	}

	public Integer getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "NodeDefinition [id=" + id + ", ip=" + ip + ", port=" + port + "]";
	}

	@Override
	public int compareTo(NodeDefinition o) {
		return getId().compareTo(o.getId());
	}
}
