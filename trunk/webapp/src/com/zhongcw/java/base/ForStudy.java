package com.zhongcw.java.base;

public class ForStudy {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			if (i == 6) {
				System.out.println("condition");
				continue;
			} else {
				System.out.println("not condition");
			}
		}
		System.out.println("end for loop");
	}

}
