package com.zhongcw.java.base.polymorphisn;

public class Parent {

	public void call() {
		System.out.println("Parent call()");
	}

	public static void main(String[] args) {
//		Parent p = new Parent();
		Parent p = new Child();
		p.call();
	}
}
