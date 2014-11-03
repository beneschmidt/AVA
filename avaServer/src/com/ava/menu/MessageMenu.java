package com.ava.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MessageMenu implements Menu {

    @Override
    public Object run() {
	System.out.println(toString());
	Object o = readInput();
	String message = o.toString();
	return message;
    }

    private String readInput() {
	boolean keepOnReading = true;
	while (keepOnReading) {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    try {
		String inputLine = br.readLine();
		return inputLine;
	    } catch (Exception e) {
		System.out.println("That was not good, try again!");
	    }
	}
	return "";
    }

    @Override
    public String toString() {
	return "Enter your message:";
    }
}
