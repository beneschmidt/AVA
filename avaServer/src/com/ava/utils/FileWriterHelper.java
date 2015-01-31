package com.ava.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

	public void writeToFile(List<String> rows) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename);
			for (String nextRow : rows) {
				fileWriter.write(nextRow + "\n");
			}
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ResourceHelper.close(fileWriter);
		}
	}
}
