package com.ava.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ava.ueb01.NodeDefinition;
import com.ava.utils.FileReaderHelper;

public class Client {
	
	private final String host;
	private final int port;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public static void main(String[] args) {
		try {
			System.out.println("Argumente:" + Arrays.toString(args));
			Map<Integer, NodeDefinition> nodes = readNodesFromFile(args[0]);
			NodeDefinition nodeDefinition = askForNodeToWorkWith(nodes);
			System.out.println("Mein Node: " + nodeDefinition);

			Socket skt = new Socket(nodeDefinition.getIp(), nodeDefinition.getPort());
			BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			System.out.print("Received string: '");

			while (!in.ready()) {
			}
			System.out.println(in.readLine()); // Read one line and output it

			System.out.print("'\n");
			in.close();
		} catch (Exception e) {
			System.out.print("Whoops! It didn't work!\n");
			e.printStackTrace();
		}
	}

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
