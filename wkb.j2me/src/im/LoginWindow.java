package im;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import util.MD5;
import util.MessageDecoder;

import model.Message;

public class LoginWindow extends Form implements CommandListener {
	private SocketMIDlet midlet;
	private Display display;
	private TextField usernameField;
	private TextField passwordField;
	private Command loginCommand = new Command("登录", Command.ITEM, 1);
	private Command exitCommand = new Command("退出", Command.EXIT, 1);

	public LoginWindow(SocketMIDlet m) {
		super("登录");
		midlet = m;
		display = Display.getDisplay(midlet);
		usernameField = new TextField("用户名：", "zn_test130", 140, TextField.ANY);
		passwordField = new TextField("密码：", "111111", 140, TextField.ANY);
		append(usernameField);
		append(passwordField);
		addCommand(exitCommand);
		addCommand(loginCommand);
		setCommandListener(this);
		display.setCurrent(this);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if ((arg0 == loginCommand) && !midlet.isPaused()) {
			final String username = usernameField.getString();
			final String password = passwordField.getString();
			final Network network = Network.getInstance();
			network.start();
			if (!network.connect("socket://10.10.71.240:8080")) { // connect LS
				Alert alert = new Alert("Client",
						"Please run Server MIDlet first", null, AlertType.ERROR);
				alert.setTimeout(Alert.FOREVER);
				alert.setCommandListener(this);
				display.setCurrent(alert);
			} else {
				Message msg = new Message();
				msg.addCmd("9000");
				msg.addCmd(username);
				network.setInputListener(new IMessageListener() {
					public void onMessage(String msg) {
						Message ret = MessageDecoder.decode(msg);
						Vector cmds = ret.getCmds();
						if ("9000".equals(cmds.elementAt(0))) {
							network.disconnect();
							String ip = ret.getCmd(2);
							String port = ret.getCmd(3);
							network.connect("socket://" + ip + ":" + port);
							Message fsMsg = new Message();
							fsMsg.addCmd("9001");
							fsMsg.addCmd(username);
							fsMsg.addCmd(new MD5().getMD5ofStr(password));
							fsMsg.addCmd(ret.getCmd(4));
							fsMsg.addCmd(username);
							network.send(fsMsg.encode());
								
							// 启动心跳
							new Thread() {
								public void run() {
									while (true) {
//										network.send("0" + "\r\n");
										network.send("0");
										try {
											sleep(3000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}.start();
							
							new ChatWindow(midlet);
						}
					}
				});
				network.getInstance().send(msg.encode());
			}
		}
	}
}
