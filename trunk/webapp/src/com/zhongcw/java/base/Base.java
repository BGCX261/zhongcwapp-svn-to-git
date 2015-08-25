package com.zhongcw.java.base;

public class Base {

	public static void main(String[] args) {
		char[] c1 = { 'z', 'c', 'w' };
		char[] c2 = { 'z', 'c', 'w' };
		String s1 = new String(c1);
		String s2 = new String(c2);
		System.out.println(s1.equals(s2));
		
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());
		/*
		 *  String类的equals()方法override Object类的equals()方法
		 *  true - 相同的字符序列
		 */

		MyObj m1 = new MyObj();
		MyObj m2 = new MyObj();
		System.out.println(m1.equals(m2));
		
		System.out.println(m1.hashCode());
		System.out.println(m2.hashCode());
		
		//toString()
		System.out.println(m1.toString());
		System.out.println(m1.getClass().getName() + "@" + Integer.toHexString(m1.hashCode()) +"_"+ m1.hashCode());
	}

}

class MyObj {

}
