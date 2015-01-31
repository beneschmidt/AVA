package com.ava;

import com.ava.node.NodeType;

public class Tst {

	public static void main(String[] args) {
		//		int start = 12000;
		//		for (int i = 1; i <= 20; i++) {
		//			System.out.println(i + " localhost:" + (start + i) + " " + getRandomType());
		//		}
		System.out.println(createResourcePropertiesFile());
	}

	//
	//	private static NodeType getRandomType() {
	//		Random random = new Random();
	//		int nextOne = random.nextInt(NodeType.values().length - 1);
	//		return NodeType.values()[nextOne];
	//
	//	}

	private static String createResourcePropertiesFile() {

		StringBuilder s = new StringBuilder();
		int start = 12000;
		int i = 1;
		s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceHandlerA).append("\n");
		i++;
		s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceHandlerB).append("\n");
		i++;

		while (i <= 20) {
			s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceWriter).append("\n");
			i++;
		}
		return s.toString();
	}
}
