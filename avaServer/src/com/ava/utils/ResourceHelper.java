package com.ava.utils;

import java.io.Closeable;

public class ResourceHelper {

	public static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// verschlucken
		}
	}
}
