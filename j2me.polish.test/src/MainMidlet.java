
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.zhongcw.TabbedFormDemo;


import de.enough.polish.ui.Alert;


public class MainMidlet extends MIDlet implements CommandListener {
	Form form;
	Command tabCmd = new Command("tab页", Command.ITEM, 8);
	Command quitCmd = new Command("Quit", Command.EXIT, 10);
	Display display;

	public MainMidlet() {
		super();
		this.form = new Form("bbb");

		//#style myItem
		TextField tf1 = new TextField("客户名称", "用友软件股份有限公司", 50, TextField.ANY);
		//#style myItem
		TextField tf2 = new TextField("联系地址", "北京市海淀区北清路68号", 50, TextField.ANY);

		this.form.append(tf1);
		this.form.append(tf2);
		this.form.addCommand(this.tabCmd);
		this.form.addCommand(this.quitCmd);
		this.form.setCommandListener(this);
	}

	protected void startApp() throws MIDletStateChangeException {
		this.display = Display.getDisplay(this);
		this.display.setCurrent(this.form);
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}

	public void commandAction(Command cmd, Displayable screen) {
		if(cmd == tabCmd){
//			Alert alert = new Alert("tab");
//			Display display = Display.getDisplay(this);
//			display.setCurrent(alert);

			new TabbedFormDemo();
		}
	}
}