package com.ava.utils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * helper class to write files
 */
public class FileWriterHelper {

	private final String filename;

	public FileWriterHelper(String filename) {
		this.filename = filename;
	}

	public void writeToFile(String text) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename);
			fileWriter.write(text);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ResourceHelper.close(fileWriter);
		}
	}
}
