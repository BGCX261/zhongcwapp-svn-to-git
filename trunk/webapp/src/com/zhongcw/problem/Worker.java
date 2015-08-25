package com.zhongcw.problem;

public class Worker implements Runnable {

	static int count = 0;
	static Object lock = new Object();

	int index;
	int pCount;

	public Worker(int index) {
		this.index = index;
	}

	public void run() {
		synchronized (lock) {
			for (;;) {
				if (count % 3 == index) {
					System.out.println(Thread.currentThread().getName());
					count++;
					pCount++;
					lock.notifyAll();
					if (pCount == 10)
						break;
				} else {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	public static void main(String[] args) {
		new Thread(new Worker(0), "A").start();
		new Thread(new Worker(1), "B").start();
		new Thread(new Worker(2), "C").start();
	}
}
