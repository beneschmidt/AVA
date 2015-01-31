package com.ava.utils;

import java.io.Closeable;

/**
 * helper class to close things for a shorter closer code block
 */
public class ResourceHelper {

	public static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// verschlucken
			System.out.println("DIS TOO CRAZY!!!!\n-------------------------");
		}
	}
}
