package com.zhuying.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static boolean isEmpty(String value) {
		return value == null || "".equals(value) || "null".equals(value);
	}

	/**
	 * 判断是否中文字符
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isChinese(String input) {
		if (input.length() != input.getBytes().length) {
			return true;
		}
		return false;
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean checkEmail(String mail) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mail);
		return m.find();
	}
}
