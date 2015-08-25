package com.zhongcw.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOStudy {
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = in.readLine();
//		while (str != null && str.length() != 0) {
//			System.out.println(str);
//		}
		
		System.out.println("=== "+str);
	}
}
