package com.zhongcw.java.base.extend;

public class SuperClass {
	public SuperClass() {
		System.out.println("父类 构造器");
		talk();
	}

	public void talk() {
		System.out.println("父类 talk()");
	}
	
	public void test() {
		System.out.println("父类 test()");
		talk();
	}
	
	public static void main(String[] args) {
		SuperClass base = new SuperClass();
	}
}
