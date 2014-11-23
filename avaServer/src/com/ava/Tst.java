package com.ava;

import java.util.Random;

import com.ava.node.NodeType;

public class Tst {

	public static void main(String[] args) {
		int start = 12000;
		for(int i = 1; i <=20; i++){
			System.out.println(i+" localhost:"+(start+i)+" "+getRandomType());
		}
	}
	
	private static NodeType getRandomType(){
		Random random = new Random();
		int nextOne = random.nextInt(NodeType.values().length -1);
		return NodeType.values()[nextOne];
		
	}
}
