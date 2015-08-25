package com.zhongcw.java.base;

public class WhileStudy {

	public static void main(String[] args) {
		int num = 1;
		while (true) {
			System.out.println(num);
			if (num == 6) {
				System.out.println("condition");
				break;
			} else {
				System.out.println("not condition");
			}
			num++;
		}// end while
		System.out.println("end while-loop");
	}

}
