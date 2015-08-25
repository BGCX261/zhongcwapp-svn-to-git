package im;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class Network {
	private static Network instance = new Network();
	private InputStream is;
	private OutputStream os;
	private SocketConnection sc;
	private boolean started;

	private MessageQueue output;
	private MessageQueue input;

	public void setInputListener(IMessageListener inputListener) {
		this.inputListener = inputListener;
	}

	private IMessageListener inputListener;

	private Network() {
		output = new MessageQueue();
		input = new MessageQueue();
	}

	public static Network getInstance() {
		return instance;
	}

	public boolean start() {
		if (started)
			return true;

		started = true;
		output.setListener(new IMessageListener() {
			public void onMessage(String msg) {
				try {
					Log.info("send =  " + msg);
					os.write(msg.getBytes());
				} catch (Exception e) {
					Log.error(e);
				}
			}
		});
		output.start();
		input.setListener(new IMessageListener() {
			public void onMessage(String msg) {
				// TODO: add decode
				Log.info("recv = " + msg);
				inputListener.onMessage(msg);
			}
		});
		input.start();

		new Thread() {
			public void run() {
				while (started) {
					try {
						int byteNum = is.available();
						if (byteNum == 0) {
							Thread.sleep(2000);
						} else {
							byte[] content = new byte[byteNum];
							is.read(content);
							String str = new String(content);
							// Log.debugMsg(str);
							input.push(str);
						}
					} catch (Exception e) {
						Log.error(e);
					}
				}
			}
		}.start();

		return true;

	}

	public boolean connect(String url) {
		try {
			sc = (SocketConnection) Connector.open(url);
			is = sc.openInputStream();
			os = sc.openOutputStream();
			return true;
		} catch (Exception e) {
			Log.error(e);
			return false;
		}
	}

	public void disconnect() {
		try {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
			if (sc != null) {
				sc.close();
			}
		} catch (IOException ioe) {
			Log.error(ioe);
		}
	}

	public void close() {
		if (!started)
			return;
		output.stop();
		input.stop();
		started = false;
	}

	public void send(String msg) {
		// TODO: add encode method
//		output.push(msg + "\r\n");
		output.push(msg);
	}

}
