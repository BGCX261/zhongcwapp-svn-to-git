package im;

import java.util.Vector;

class MessageQueue extends Thread {
	private Vector vec = new Vector();
	private IMessageListener listener;
	private boolean started = true;

	public IMessageListener getListener() {
		return listener;
	}

	public void setListener(IMessageListener listener) {
		this.listener = listener;
	}

	public synchronized void push(String msg) {
		vec.addElement(msg);
		this.notifyAll();
	}

	public synchronized String pop() {
		while (vec.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String str = (String) vec.elementAt(0);
		vec.removeElementAt(0);
		return str;
	}

	public void run() {
		while (started) {
			if (listener != null) {
				String msg = pop();
				listener.onMessage(msg);
			}
		}
	}

	public void stop() {
		started = false;
	}
}
