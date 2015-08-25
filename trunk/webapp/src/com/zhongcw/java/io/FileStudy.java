package com.zhongcw.java.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileStudy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileStudy fs = new FileStudy();
		fs.readFile("com/zhongcw/java/io/10086.txt");
//		fs.writeFile("F:\\javaIO3.txt");
	}

	private void readFile(String pathname) {
		try {
//			File file = new File(pathname);
//			InputStream is = new FileInputStream(file);
			
			//相对路径
			InputStream is =FileStudy.class.getClassLoader().getResourceAsStream(pathname);
			StringBuffer buffer = new StringBuffer();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					is));
			line = reader.readLine(); // 读取一个文本行
			while (line != null) {
				buffer.append(line);
				buffer.append("\n");
				line = reader.readLine();
			}
			is.close(); // 关闭此输入流并释放与该流关联的所有系统资源。

			System.out.println(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeFile(String pathname) {
		File file = new File(pathname);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("java写文件测试");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
