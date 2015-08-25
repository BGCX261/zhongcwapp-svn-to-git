package com.zhongcw.java.concurrency;

import org.apache.catalina.ant.StartTask;

public class YieldThread extends Thread {

	private int countDown = 5;
	private static int threadCount = 9;

	YieldThread() {
		super("" + ++threadCount);
		start();
	}

	public void run() {
		while (true) {
			System.out.println(this);
			if (--countDown == 0) {
				return;
			}
			// TODO yield 让步
			yield();
		}
	}

	public String toString() {
		return "Therad " + getName() + " : " + countDown ;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			new YieldThread();
		}
	}

}
