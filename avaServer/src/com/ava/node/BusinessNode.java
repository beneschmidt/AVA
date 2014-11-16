package com.ava.node;

import java.util.Random;

public class BusinessNode extends Node {

	public static final int MIN_ETAT = 10;
	public static final int MAX_ETAT = 20;

	private int etat;

	public BusinessNode(NodeDefinition nodeDefinition) {
		super(nodeDefinition);
		Random rand = new Random();
		int randomNum = rand.nextInt((MAX_ETAT - MIN_ETAT) + 1) + MIN_ETAT;
		this.etat = randomNum;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}
	
	public void adPlaced(){
		etat--;
	}
	
	public boolean canIPlaceMoreAds(){
		return etat>0;
	}

	@Override
	public String toString() {
		return "BusinessNode [etat=" + etat + ", toString()=" + super.toString() + "]";
	}

}
