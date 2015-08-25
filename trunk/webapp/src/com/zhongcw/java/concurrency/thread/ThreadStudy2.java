package com.zhongcw.java.concurrency.thread;

public class ThreadStudy2 implements Runnable {

	public void run() {
		System.out.println("create a new thread by implement interface(Runnable)");
	}

	public static void main(String[] args) {
		ThreadStudy2 ts2 = new ThreadStudy2();
		new Thread(ts2).start();
	}

}
