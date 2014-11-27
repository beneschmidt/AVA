package com.ava.utils;

public class ValueHelper {

	/**
	 * cyclic check for int[i-1] = int[i]. If at least one of these comparisons is false, the values aren't all equal.
	 * Special case: zero or one value are always true
	 * @param values
	 * @return all values equal to each other?
	 */
	public static boolean allEqual(int... values) {
		if (values.length < 2) {
			return true;
		} else {
			for (int next = 1; next < values.length; next++) {
				if (values[next - 1] != values[next]) {
					return false;
				}
			}
			return true;
		}
	}
}
