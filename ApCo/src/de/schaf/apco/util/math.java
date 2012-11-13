package de.schaf.apco.util;

public class math {

	public static int nextMultipleOfTwo(int a) {
		int i = 1;
		while (a > Math.pow(2, i)) {
			i++;
		}
		return (int)Math.pow(2, i);
	}

}
