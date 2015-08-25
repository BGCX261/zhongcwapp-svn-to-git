package com.ufida.gkb.util;

public class StringUtil {
	public static boolean isEmpty(String value) {
		return value == null || "".equals(value) || "null".equals(value);
	}
}
