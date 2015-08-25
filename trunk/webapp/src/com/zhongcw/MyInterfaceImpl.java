package com.zhongcw;

public class MyInterfaceImpl implements MyInterface {

	@Override
	public void init() {
		System.out.println("init(),MyInterfaceImpl");
	}

	@Override
	public void start() {
		System.out.println("start(),MyInterfaceImpl");
	}

}
