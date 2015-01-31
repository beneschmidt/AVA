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
	private final NodeType nodeType;

	/**
	 * Node definition can be created through a given string
	 * @param stringDefinition
	 */
	public NodeDefinition(String stringDefinition) {
		List<String> values = parseDefinitionByString(stringDefinition);
		id = Integer.parseInt(values.get(0));
		ip = values.get(1);
		port = Integer.parseInt(values.get(2));
		nodeType = NodeType.valueOf(values.get(3));
	}

	public NodeDefinition(Integer id, String ip, int port, NodeType nodeType) {
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.nodeType = nodeType;
	}

	private List<String> parseDefinitionByString(String stringDefinition) {
		StringBuilder s = new StringBuilder("((");
		for (NodeType type : NodeType.values()) {
			s.append(type).append("|");
		}
		s.deleteCharAt(s.lastIndexOf("|"));
		s.append("))");

		String pattern = "(\\d{0,3}) (.*):(\\d{1,6}) " + s.toString();
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);
		List<String> values = new LinkedList<String>();

		// Now create matcher object.
		Matcher m = r.matcher(stringDefinition);
		if (m.find()) {
			values.add(m.group(1));
			values.add(m.group(2));
			values.add(m.group(3));
			values.add(m.group(4));
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

	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public String toString() {
		return nodeType.toString().toUpperCase() + " [id=" + id + ", ip=" + ip + ", port=" + port + "]";
	}

	@Override
	public int compareTo(NodeDefinition o) {
		return getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NodeDefinition) {
			return compareTo((NodeDefinition) obj) == 0;
		}
		return false;
	}
}
