package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.ava.node.Node;
import com.ava.node.NodeDefinition;

/**
 * menu to select a specific node
 */
public class NodeSelectionMenu implements Menu {

	private Node node;

	public NodeSelectionMenu(Node node) {
		super();
		this.node = node;
	}

	@Override
	public Object run() {
		while (true) {
			System.out.println(toString());
			return readInput();
		}
	}

	private NodeDefinition readInput() {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter selection:");
			try {
				String inputLine = br.readLine();
				Integer id = Integer.parseInt(inputLine);
				if (node.getConnectedSockets().size() >= id) {
					int i = 1;
					for (NodeDefinition definition : node.getConnectedSockets().keySet()) {
						if (i == id) {
							return definition;
						} else {
							i++;
						}
					}
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder menu = new StringBuilder().append("Select Node: \n");
		int i = 1;
		for (NodeDefinition definition : node.getConnectedSockets().keySet()) {
			menu.append(i).append(") ").append(definition).append("\n");
			i++;
		}
		return menu.toString();
	}

}
