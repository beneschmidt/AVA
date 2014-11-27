package com.ava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * helper class for time concerns
 */
public class TimeUtils {

	public static String getCurrentTimestampString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return sdf.format(date);
	}

	public static String getCurrentTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return sdf.format(date);
	}

	public static void sleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println("ain't noboy got time fo dat");
		}
	}
}
