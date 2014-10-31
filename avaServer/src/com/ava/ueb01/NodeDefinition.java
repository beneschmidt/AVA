package com.ava.ueb01;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entspricht einer Knotendefinition mit entsprechender id, IP und einem Port
 * 
 * @author D063416
 */
public class NodeDefinition {

	private final int id;
	private final String ip;
	private final int port;

	public NodeDefinition(String stringDefinition) {
		List<String> values = parseDefinitionByString(stringDefinition);
		id = Integer.parseInt(values.get(0));
		ip = values.get(1);
		port = Integer.parseInt(values.get(2));
	}

	private List<String> parseDefinitionByString(String stringDefinition){
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
			throw new RuntimeException("Kann String leider nicht parsen!"
					+ stringDefinition);
		}
	}

	public int getId() {
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
		return "NodeDefinition [id=" + id + ", ip=" + ip + ", port=" + port
				+ "]";
	}

	public static void main(String[] args) throws Exception {
		NodeDefinition nd = new NodeDefinition("1 isl-s-01:5000");
		System.out.println(nd);
	}
}