package com.ava.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Hilfeklasse zum Auslesen von Dateien
 * 
 * @author Benedikt Schmidt
 */
public class FileReaderHelper {

    private final String filename;

    public FileReaderHelper(String filename) {
	this.filename = filename;
    }

    public List<String> readFileAsRows() {
	List<String> lines = new LinkedList<String>();
	BufferedReader br = null;
	try {
	    FileReader fileReader = new FileReader(filename);
	    br = new BufferedReader(fileReader);
	    String line = "";
	    while ((line = br.readLine()) != null) {
		lines.add(line);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    ResourceHelper.close(br);
	}
	return lines;
    }

    public String readFileAsOneString() {
	StringBuilder oneString = new StringBuilder();
	List<String> rows = readFileAsRows();
	for (String nextRow : rows) {
	    oneString.append(nextRow).append("\n");
	}
	return oneString.toString();
    }
}
