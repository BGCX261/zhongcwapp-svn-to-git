package com.zhongcw.java.concurrency.thread;

/////
//ProducerConsumer.java

//
//这是个很重要的Thread例子。需要注意的是：
//wait() 必须在synchronized 函数或者代码块里面
//wait()会让已经获得synchronized 函数或者代码块控制权的Thread暂时休息，并且丧失控制权
//这个时候，由于该线程丧失控制权并且进入等待，其他线程就能取得控制权，并且在适当情况下调用notifyAll()来唤醒wait()的线程。
//需要注意的是，被唤醒的线程由于已经丧失了控制权，所以需要等待唤醒它的线程结束操作，从而才能重新获得控制权。
//
//所以wait()的确是马上让当前线程丧失控制权，其他的线程可以乘虚而入。
//
//所以wait()的使用，必须存在2个以上线程，而且必须在不同的条件下唤醒wait()中的线程。
//
//
//以下的例子：
//ProductStack 是一个生产者跟消费者共享的同步机制，这个机制决定了什么情况生产者要wait()，什么情况消费者要wait()
//可以把ProductStack看作一个产品仓库。当产品仓库满的时候，生产者线程需要wait()，从而放弃对产品仓库的控制。
//这个时候消费者线程就可以进来了而取得仓库的控制权。一旦消费者消费了产品，那么仓库就不满了。
//这个时候消费者线程就要notifyAll()生产者线程，让等待的生产者线程唤醒。
//但是生产者被唤醒后不能马上进行生产，因为它在wait()的时候已经丧失了对仓库的控制权，所以就需要等待消费者线程结束操作，
//才能重新取得仓库的控制权，再进行生产。
//
//所以特别注意的是，notifyAll()并不是让当前线程马上让出控制权，而只是让其他wait()当中的线程唤醒而已，
//所以对不起，尽管我唤醒你，可你必须还是要等我用完仓库才能进来。这点必须清楚。
//
//相反，仓库如果空的时候，消费者线程就会wait()，然后等待生产者线程来生产产品，生产者进程乘虚而入后，让生产者线程生产产品
//并且唤醒消费者线程。这个情况跟上面就类似了。
//
///

public class ProducerConsumer {
	public static void main(String[] args) {
		ProductStack ps = new ProductStack();
		Producer p = new Producer(ps, "生产者1");
		Consumer c = new Consumer(ps, "消费者1");
		new Thread(p).start();
		new Thread(c).start();
	}
}

/**
 * 产品
 */
class Product {
	int id;
	private String producedBy = "N/A";
	private String consumedBy = "N/A";

	// 构造函数，指明产品ID以及生产者名字。
	Product(int id, String producedBy) {
		this.id = id;
		this.producedBy = producedBy;
	}

	// 消费，需要指明消费者名字
	public void consume(String consumedBy) {
		this.consumedBy = consumedBy;
	}

	public String toString() {
		return "Product : " + id + ", produced by " + producedBy
				+ ", consumed by " + consumedBy;
	}

	public String getProducedBy() {
		return producedBy;
	}

	public void setProducedBy(String producedBy) {
		this.producedBy = producedBy;
	}

	public String getConsumedBy() {
		return consumedBy;
	}

	public void setConsumedBy(String consumedBy) {
		this.consumedBy = consumedBy;
	}
}

// 这个class就是仓库，是生产者跟消费者共同争夺控制权的同步资源
class ProductStack {
	int index = 0;
	Product[] arrProduct = new Product[6];

	// push是用来让生产者放置产品的
	public synchronized void push(Product product) {
		// 如果仓库满了
		while (index == arrProduct.length) // 这里本来可以用if(),但是如果catch
		// exception会出问题，让满的index越界
		{
			try {
				// here, "this" means the thread that is using "push"
				// so in this case it's a producer thread instance.
				// the BIG difference between sleep() and wait() is, once
				// wait(),
				// the thread won't have the lock anymore
				// so when a producer wait() here, it will lost the lock of
				// "push()"
				// While sleep() is still keeping this lock
				// Important: wait() and notify() should be in "synchronized"
				// block

				System.out.println(product.getProducedBy() + " is waiting.");
				// 等待，并且从这里退出push()
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end while

		System.out.println(product.getProducedBy() + " sent a notifyAll().");

		// 因为我们不确定有没有线程在wait()，所以我们既然生产了产品，就唤醒有可能等待的消费者，让他们醒来，准备消费
		notifyAll();

		// 注意，notifyAll()以后，并没有退出，而是继续执行直到完成。
		arrProduct[index] = product;
		index++;
		System.out.println(product.getProducedBy() + " 生产了: " + product);
	}

	// pop用来让消费者取出产品的
	public synchronized Product pop(String consumerName) {
		// 如果仓库空了
		while (index == 0) {
			try {
				// here will be the consumer thread instance will be waiting ,
				// because empty
				System.out.println(consumerName + " is waiting.");
				// 等待，并且从这里退出pop()
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end while

		System.out.println(consumerName + " sent a notifyAll().");

		// 因为我们不确定有没有线程在wait()，所以我们既然消费了产品，就唤醒有可能等待的生产者，让他们醒来，准备生产
		notifyAll();

		// 注意，notifyAll()以后，并没有退出，而是继续执行直到完成。
		// 取出产品
		index--;
		Product product = arrProduct[index];
		product.consume(consumerName);
		System.out.println(product.getConsumedBy() + " 消费了: " + product);
		return product;
	}
}

/**
 * 生产者
 */
class Producer implements Runnable {
	String name;
	ProductStack ps = null;

	Producer(ProductStack ps, String name) {
		this.ps = ps;
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			Product product = new Product(i, name);
			ps.push(product);
			try {
				Thread.sleep((int) (Math.random() * 200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * 消费者
 */
class Consumer implements Runnable {
	String name;
	ProductStack ps = null;

	Consumer(ProductStack ps, String name) {
		this.ps = ps;
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			Product product = ps.pop(name);
			try {
				Thread.sleep((int) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
