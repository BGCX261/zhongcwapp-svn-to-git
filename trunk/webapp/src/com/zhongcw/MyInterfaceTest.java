package com.zhongcw;

public class MyInterfaceTest {

	public static void main(String[] args) {
		invokeInterface(new MyInterfaceImpl());
	}

	private static void invokeInterface(MyInterface myInterface) {
		myInterface.init();
		myInterface.start();
	}

}
