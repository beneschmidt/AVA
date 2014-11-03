package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import com.ava.node.Node;

public class MainMenu implements Menu {
    private static Map<Integer, String> selections;
    static {
	selections = new TreeMap<>();
	selections.put(1, "close connections to other sockets");
	selections.put(2, "close connections and exit");
	selections.put(3, "send a message to a socket");
	selections.put(4, "send a message to all sockets");
    }

    private Node node;

    public MainMenu(Node node) {
	this.node = node;
    }

    @Override
    public String toString() {
	StringBuilder menu = new StringBuilder().append("What do you want to do?\n");
	for (Map.Entry<Integer, String> entries : selections.entrySet()) {
	    menu.append(entries.getKey()).append(") ").append(entries.getValue()).append("\n");
	}
	return menu.toString();
    }

    @Override
    public void handleInput(Object o) {
	try {
	    Integer idToHandle = Integer.valueOf(o.toString());
	    switch (idToHandle) {
	    case 1:
		node.closeAllConnections();
		break;
	    case 2:
		node.closeAllConnections();
		node.closeServer();
		System.exit(0);
	    case 3:
		break;
	    case 4:
		break;
	    default:
		System.out.println("wat?");
		break;
	    }
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

    }

    @Override
    public void start() {
	while (true) {
	    System.out.println(toString());
	    Object o = readInput();
	    handleInput(o);
	}
    }

    private Integer readInput() {
	boolean keepOnReading = true;
	while (keepOnReading) {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print("Enter selection:");
	    try {
		String inputLine = br.readLine();
		Integer id = Integer.parseInt(inputLine);
		if (selections.containsKey(id)) {
		    return id;
		} else {
		    System.out.println("Id not found! try again!");
		}
	    } catch (Exception e) {
		System.out.println("Not a number, try again!");
	    }
	}
	return 0;
    }
}
