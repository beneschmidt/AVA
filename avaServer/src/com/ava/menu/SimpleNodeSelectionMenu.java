package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import com.ava.node.NodeDefinition;

public class SimpleNodeSelectionMenu implements Menu {

	private final Map<Integer, NodeDefinition> nodes;

	public SimpleNodeSelectionMenu(Map<Integer, NodeDefinition> nodes) {
		this.nodes = nodes;
	}

	@Override
	public Object run() {
		boolean keepOnReading = true;
		while (keepOnReading) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter id:");
			try {
				String inputLine = br.readLine();
				Integer id = Integer.parseInt(inputLine);
				if (nodes.get(id) != null) {
					return nodes.get(id);
				} else {
					System.out.println("Id not found! try again!");
				}
			} catch (Exception e) {
				System.out.println("Not a number, try again!");
			}
		}
		return null;
	}

}
