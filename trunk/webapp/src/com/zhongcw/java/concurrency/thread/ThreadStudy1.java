package com.zhongcw.java.concurrency.thread;

public class ThreadStudy1 extends Thread {
	private int count = 1;

	public void run() {
		while (true) {
			if (count > 10) {
				System.out.println("if condition...");
//				break;
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
//				try {
//					wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			System.out.println("run " + count);
			count++;
		}
	}

	public static void main(String[] args) {
		ThreadStudy1 ts1 = new ThreadStudy1();
		ts1.start();
	}

}
