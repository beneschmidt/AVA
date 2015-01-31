package com.ava;

import com.ava.node.NodeType;
import com.ava.utils.FileWriterHelper;

public class Tst {

	private static final int NODE_SIZE = 10;

	public static void main(String[] args) {
		//		int start = 12000;
		//		for (int i = 1; i <= 20; i++) {
		//			System.out.println(i + " localhost:" + (start + i) + " " + getRandomType());
		//		}
		FileWriterHelper helper = new FileWriterHelper("resources/ueb3_input.txt");
		helper.writeToFile(createResourceInputPropertyFile(NODE_SIZE));

		FileWriterHelper helper2 = new FileWriterHelper("resources/ueb3_startup.bat");
		helper2.writeToFile(createResourceStartupPropertyFile(NODE_SIZE));
	}

	//
	//	private static NodeType getRandomType() {
	//		Random random = new Random();
	//		int nextOne = random.nextInt(NodeType.values().length - 1);
	//		return NodeType.values()[nextOne];
	//
	//	}

	private static String createResourceInputPropertyFile(int nodeCount) {
		StringBuilder s = new StringBuilder();
		int start = 12000;
		int i = 1;
		s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceHandlerA).append("\n");
		i++;
		s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceHandlerB).append("\n");
		i++;

		while (i <= nodeCount) {
			s.append(i + " localhost:" + (start + i) + " " + NodeType.resourceWriter).append("\n");
			i++;
		}
		return s.toString();
	}

	private static String createResourceStartupPropertyFile(int nodeCount) {
		StringBuilder s = new StringBuilder();
		s.append("for /l %%x in (1, 1, ").append(nodeCount).append(") do (\n").append("start cmd /k java -jar Resource.jar ueb3_input.txt %%x\n)");
		return s.toString();
	}
}
