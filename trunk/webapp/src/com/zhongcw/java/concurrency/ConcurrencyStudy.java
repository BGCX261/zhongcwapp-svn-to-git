package com.zhongcw.java.concurrency;

import org.apache.catalina.ant.StartTask;

public class ConcurrencyStudy extends Thread {

	private int countDown = 5;
	private static int threadCount = 9;

	ConcurrencyStudy() {
		super("" + ++threadCount);
		// TODO 如果不调用start(),线程永远不会启动
		start();
	}

	public void run() {
		while (true) {
			System.out.println(this);
			if (--countDown == 0) {
				return;
			}
		}
	}

	public String toString() {
		return "Therad " + getName() + " : " + countDown ;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new ConcurrencyStudy();
		}
	}

}
