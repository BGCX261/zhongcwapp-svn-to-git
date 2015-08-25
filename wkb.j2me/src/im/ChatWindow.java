package im;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import model.Message;

public class ChatWindow extends Form implements CommandListener {
	private SocketMIDlet midlet;
	private Display display;
	private StringItem stringItem;
	private TextField textField;
	private Command sendCommand = new Command("发送", Command.ITEM, 1);
	private Command exitCommand = new Command("退出", Command.EXIT, 1);

	public ChatWindow(SocketMIDlet m) {
		super("聊天窗口");
		midlet = m;
		display = Display.getDisplay(midlet);
		stringItem = new StringItem("接收：", " ");
		textField = new TextField("输入：", "",
				140, TextField.ANY);
		append(stringItem);
		append(textField);
		addCommand(exitCommand);
		addCommand(sendCommand);
		setCommandListener(this);
		display.setCurrent(this);
	}

	public void commandAction(Command cmd, Displayable s) {
		if ((cmd == sendCommand) && !midlet.isPaused()) {
			Network.getInstance().send(textField.getString());
		}
		if ((cmd == Alert.DISMISS_COMMAND) || (cmd == exitCommand)) {
			midlet.notifyDestroyed();
			midlet.destroyApp(true);
		}
	}

	public void stop() {
		Network.getInstance().close();
	}
}
