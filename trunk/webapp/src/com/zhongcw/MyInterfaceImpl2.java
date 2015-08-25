package com.zhongcw;

public class MyInterfaceImpl2 implements MyInterface {

	@Override
	public void init() {
		System.out.println("init(),MyInterfaceImpl2...");
	}

	@Override
	public void start() {
		System.out.println("start(),MyInterfaceImpl2...");
	}

}
