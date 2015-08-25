package com.zhongcw.java.concurrency;

import org.apache.catalina.ant.StartTask;

public class ThreadPriority extends Thread {

	private int countDown = 5;
	
	private volatile double d = 0;

	ThreadPriority(int priority) {
		System.out.println("constructor");
		setPriority(priority);
		start();
	}

	public void run() {
		System.out.println("run");
		while (true) {
			for(int i=1;i<100000;i++){
				d = d + (Math.PI + Math.E) / (double)i;
				System.out.println(this);
				if (--countDown == 0) {
					return;
				}
			}
		}
	}

	public String toString() {
		return super.toString() + " : " + countDown ;
	}

	public static void main(String[] args) {
		System.out.println("main");
		new ThreadPriority(Thread.MAX_PRIORITY);
		
		for (int i = 0; i < 3; i++) {
			new ThreadPriority(Thread.MIN_PRIORITY);
		}
	}

}
