package com.zhongcw.problem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Rfile {
	public static File f;
	public static int ch;
	public static int u = 0;// 大写字母个数
	public static int l = 0;// 小写字母个数
	public static int m = 0;// 数字个数
	public static int o = 0;// 其他
	public static int b = 0;// 空格
	public static int line = 0;// 行数

	public static void main(String[] args) throws Exception {
		f = new File("D:/zcw.txt");
		FileInputStream fis = new FileInputStream(f);
		while ((ch = fis.read()) != -1) {
			System.out.println("askii = "+ch);
			
			if(ch == 10){
				line++;
			}
			
			char c = (char) ch;
			System.out.println("char = "+c);
			System.out.println();
			
			if (Character.isUpperCase(c)) {
				u++;
			} else if (Character.isLowerCase(c)) {
				l++;
			} else if (Character.isDigit(c)) {
				m++;
			} else if(Character.isSpaceChar(c)){
				b++;
			}else{
				o++;
			}
		}
		
		System.out.println("大写字母: " + u + "\n" 
						+ "小写字母: " + l + "\n" 
						+ "数字: " + m + "\n" 
						+ "空格: " + b + "\n" 
						+ "行数: " + (line + 1) + "\n" 
						+ "其他: " + o);
		
//		FileOutputStream fout = new FileOutputStream(new File("D:/log.txt"));
//		fout.write("wahaha".getBytes());
//		fout.close();
		
	}

}
