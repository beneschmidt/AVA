package com.ava.utils;

import java.io.Closeable;
import java.io.IOException;

public class ResourceHelper {

	public static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			// verschlucken
		}
	}
}
