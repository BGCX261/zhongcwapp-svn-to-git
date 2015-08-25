package com.zhuying.android.util;

import java.security.MessageDigest;

public class Md5 {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String md5(byte[] bytes) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes, 0, bytes.length);
		return bufferToHex(messageDigest.digest());
	}
	
	public static String md5(String str) throws Exception{
		return md5(str.getBytes());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[bt >> 4 & 0X0F];
		char c1 = hexDigits[bt & 0X0F];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
