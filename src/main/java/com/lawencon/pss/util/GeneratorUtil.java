package com.lawencon.pss.util;

import java.util.Random;

public class GeneratorUtil {

	public static int randomInt(int max) {
		final Random random = new Random();
		return random.nextInt(max);
	}
	
	public static String randomString() {
		// Copied from : https://www.baeldung.com/java-random-string -- with edit
	    final int leftLimit = 48;
	    final int rightLimit = 90;
	    final int targetStringLength = 5;
	    final Random random = new Random();

	    final String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString().toUpperCase();
		
		return generatedString;
	}
}
