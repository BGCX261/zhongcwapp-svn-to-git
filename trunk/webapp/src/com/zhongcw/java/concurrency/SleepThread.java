package com.zhongcw.java.concurrency;

import org.apache.catalina.ant.StartTask;

public class SleepThread extends Thread {

	private int countDown = 5;
	private static int threadCount = 9;

	SleepThread() {
		super("" + ++threadCount);
		start();
	}

	public void run() {
		while (true) {
			System.out.println(this);
			if (--countDown == 0) {
				return;
			}
			// TODO sleep 休眠
			try {
				sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String toString() {
		return "Therad " + getName() + " : " + countDown;
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 3; i++) {
			new SleepThread().join();
		}
	}

}
