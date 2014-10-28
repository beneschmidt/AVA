package com.ava.ueb01;


public class LocalNodeImplenentation {

	public static void main(String[] args) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("java",
					"-jar", "C:\\Users\\D063416\\Desktop\\test.jar", "localhost", "1234", "C:\\Users\\D063416\\Desktop\\input.txt");
			processBuilder.redirectErrorStream(true);
			processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			processBuilder.start();
		} catch (Exception e) {
			// TODO Fehlerhandling
			e.printStackTrace();
		}
	}
}
