package com.zhongcw.problem;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		 /**
		  * 字符集编码
		  */
		 String str = "娃哈哈";
		 System.out.println(new String(str.getBytes("utf-8")));

		/**
		 * switch语句
		 */
		int key = 2;
		switch (key) {
		case 1:
			System.out.println("case 1");
			break;
		case 2:
			System.out.println("case 2");
			break;
		default:
			System.out.println("default");
			break;
		}
		
	}
	
}
