package com.ava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.client.Node;
import com.ava.ueb01.NodeDefinition;
import com.ava.utils.FileReaderHelper;

public class Startup {

    public static void main(String[] args) {
	try {
	    System.out.println("Argumente:" + Arrays.toString(args));
	    Map<Integer, NodeDefinition> nodes = readNodesFromFile(args[0]);
	    NodeDefinition nodeDefinition = askForNodeToWorkWith(nodes);
	    System.out.println("Mein Node: " + nodeDefinition);
	    final Node node = new Node(nodeDefinition);
	    new Thread(new Runnable() {
		public void run() {
		    node.startServer();
		}
	    }).start();
	    node.connectToOtherNodes(nodes);
	    node.broadcastMessage();
	    System.out.println("start sleeping");
	    Thread.sleep(10000);
	    node.closeAllConnections();
	} catch (Exception e) {
	    System.out.print("Whoops! It didn't work!\n");
	    e.printStackTrace();
	}
    }

    /**
     * Fraegt uber die Konsole die Node-ID ab und sucht aus den Nodes den
     * entsprechenden Eintrag
     * 
     * @param nodes
     * @return gesuchten Node
     * @throws Exception
     */
    private static NodeDefinition askForNodeToWorkWith(Map<Integer, NodeDefinition> nodes) throws Exception {
	boolean keepOnReading = true;
	while (keepOnReading) {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print("Enter id:");
	    String inputLine = br.readLine();
	    try {
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
	throw new Exception("nothing found!");
    }

    /**
     * Liest alle Node-Definitionen aus einer gegebenen Datei aus
     * 
     * @param filename
     * @return Map mit Nodes
     */
    private static Map<Integer, NodeDefinition> readNodesFromFile(String filename) {
	FileReaderHelper fileReaderHelper = new FileReaderHelper(filename);
	Map<Integer, NodeDefinition> nodes = new TreeMap<Integer, NodeDefinition>();
	List<String> lines = fileReaderHelper.readFileAsRows();
	for (String nextLine : lines) {
	    NodeDefinition nodeDefinition = new NodeDefinition(nextLine);
	    nodes.put(nodeDefinition.getId(), nodeDefinition);
	    System.out.println(nextLine);
	}
	System.out.println("Insgesamt gefunden: " + nodes.size());
	return nodes;
    }
}