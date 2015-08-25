package com.zhongcw.java;

public class ThreadStudy extends Thread {

	public void run() {
//		for (int i = 0; i < 5; i++) {
//			System.out.println(i);
//		}
		while(true){
			System.out.println("...");
		}
	}

	public static void main(String[] args) {
		// new Thread() {
		// public void run() {
		// for (int i = 0; i < 5; i++) {
		// System.out.println(i);
		// if (i == 2) {
		// try {
		// sleep(6000);
		// // this.wait(6000);
		// System.out.println("sleep");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		// }.start();
	}

}
