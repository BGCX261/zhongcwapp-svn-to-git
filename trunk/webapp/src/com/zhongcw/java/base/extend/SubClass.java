package com.zhongcw.java.base.extend;

public class SubClass extends SuperClass {

	public SubClass() {
		System.out.println("子类 构造器");
	}

	@Override
	public void talk() {
		System.out.println("子类 talk()");
		
	}

	public static void main(String[] args) {
		SubClass sub = new SubClass();
		sub.talk();
		sub.test();
	}
}
