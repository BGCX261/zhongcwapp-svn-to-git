package com.zhongcw.java;

public class MyThread {
	public static void main(String[] args) {
		ThreadStudy t = new ThreadStudy();
		t.start();
		
		try {
//			t.sleep(5000);
			t.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
