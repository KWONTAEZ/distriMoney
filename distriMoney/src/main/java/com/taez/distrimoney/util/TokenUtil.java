package com.taez.distrimoney.util;

import java.util.concurrent.ThreadLocalRandom;

public class TokenUtil {

	public static final String TOKEN_VALS = "0123456789abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ";

	public static String createToken() {
		StringBuffer sb = new StringBuffer();
		int randomIdx = ThreadLocalRandom.current().nextInt(3844, 238328);
		int tokenValueSize = TOKEN_VALS.length();
		sb = new StringBuffer();
		do {
			
			sb.append(TOKEN_VALS.charAt(randomIdx % tokenValueSize));
			randomIdx /= tokenValueSize;
		} while (randomIdx != 0);

		return sb.toString();
	}

}
