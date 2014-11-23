package com.ava.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BusinessPropertiesReader {

	/**
	 * @param filename
	 * @return Map with node definitions
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Properties readProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("business.properties"));
		System.out.println(properties.values());
		return properties;
	}
}
