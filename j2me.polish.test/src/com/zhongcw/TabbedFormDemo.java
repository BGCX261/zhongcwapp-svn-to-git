package com.zhongcw;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import de.enough.polish.ui.Screen;
import de.enough.polish.ui.ScreenStateListener;
import de.enough.polish.ui.TabbedForm;
import de.enough.polish.ui.UiAccess;
import de.enough.polish.util.Locale;

public class TabbedFormDemo extends MIDlet implements ScreenStateListener,
		CommandListener {

	private Command exitCmd = new Command(Locale.get("cmd.exit"), Command.BACK,
			2);
	private Command backCmd = new Command(Locale.get("cmd.back"), Command.BACK,
			2);
	private Command parentCmd = new Command(Locale.get("cmd.modetypes"),
			Command.SCREEN, 20);
	private Command playerCmd = new Command(Locale.get("cmd.player"),
			Command.SCREEN, 1);
	private Command adversaryCmd = new Command(Locale.get("cmd.adversary"),
			Command.SCREEN, 2);
	private TabbedForm tabbedForm;
	private int lastTabIndex;

	public TabbedFormDemo() {
		super();

		Alert alert = new Alert("constructor...");
		Display display = Display.getDisplay(this);
		display.setCurrent(alert);

		//#debug
		System.out.println("Creating TabbedFormDemo MIDlet.");
	}

	protected void startApp() throws MIDletStateChangeException {
		//#debug
		System.out.println("Starting FramedFormDemo MIDlet.");
		String[] headings = new String[] { Locale.get("title.introduction"),
				Locale.get("title.setting"), Locale.get("title.input") };
		//#style tabbedForm
		TabbedForm form = new TabbedForm(Locale.get("title.main"), headings,
				null);

		//#style label
		StringItem label = new StringItem(null, Locale.get("txt.introduction"));
		form.append(0, label);

		//#style settingsChoice
		ChoiceGroup group = new ChoiceGroup(Locale.get("label.setting"),
				ChoiceGroup.EXCLUSIVE);
		//#style settingsItem
		group.append(Locale.get("label.setting.fast"), null);
		//#style settingsItem
		group.append(Locale.get("label.setting.normal"), null);
		//#style settingsItem
		group.append(Locale.get("label.setting.slow"), null);
		group.setSelectedIndex(0, true);
		form.append(1, group);
		// label = new StringItem( null, "click me", StringItem.BUTTON );
		// form.append( 1, label );

		//#style input
		TextField field = new TextField(Locale.get("label.input"), "\n", 30,
				TextField.ANY);
		UiAccess.setInputMode(field, UiAccess.MODE_NATIVE);
		form.append(2, field);
		//#style input
		field = new TextField("Nick: ", "", 30, TextField.ANY);
		form.append(2, field);

		form.addCommand(this.exitCmd);
		form.setScreenStateListener(this);
		form.setCommandListener(this);
		this.tabbedForm = form;

		Display display = Display.getDisplay(this);
		display.setCurrent(form);

		//#debug
		System.out.println("TabbedFormDemo MIDlet is up and running.");
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}

	public void commandAction(Command cmd, Displayable disp) {
		//#debug
		System.out.println("commandAction( " + cmd.getLabel() + ", " + disp
				+ ")");
		if (cmd == this.exitCmd) {
			notifyDestroyed();
		} else if (cmd == this.backCmd) {
			this.tabbedForm.setActiveTab(this.lastTabIndex - 1);
			// manually call screenStateChanged, so that commands are updated
			// accordingly:
			screenStateChanged(this.tabbedForm);
		} else if (cmd == this.playerCmd) {
			try {
				Image image = Image.createImage("/icon.png");
				this.tabbedForm.setTabImage(0, image);
			} catch (Exception e) {
			}
		} else {
			//#style notificationAlert
			Alert alert = new Alert("Notification", "The command \""
					+ cmd.getLabel() + "\" is currently not supported.", null,
					AlertType.INFO);
			Display display = Display.getDisplay(this);
			display.setCurrent(alert);
		}
	}

	public void screenStateChanged(Screen screen) {
		if (screen == this.tabbedForm) {
			int tabIndex = this.tabbedForm.getActiveTab();
			if (tabIndex != this.lastTabIndex) {
				//#debug
				System.out.println("ScreenStateChanged: new tab=" + tabIndex);
				if (tabIndex == 0) {
					this.tabbedForm.removeCommand(this.backCmd);
					this.tabbedForm.removeCommand(this.parentCmd);
					this.tabbedForm.addCommand(this.exitCmd);
				} else if (tabIndex == 1 && this.lastTabIndex == 0) {
					this.tabbedForm.removeCommand(this.exitCmd);
					this.tabbedForm.addCommand(this.backCmd);
					// for (int i = 0; i < 20; i++) {
					// this.tabbedForm.addCommand( new Command( getText(i),
					// Command.SCREEN, i % 14 + 2) );
					// }
					this.tabbedForm.addCommand(this.parentCmd);
					UiAccess.addSubCommand(this.playerCmd, this.parentCmd,
							this.tabbedForm);
					UiAccess.addSubCommand(this.adversaryCmd, this.parentCmd,
							this.tabbedForm);
					// UiAccess.addSubCommand( new
					// Command("hi1 and some longer text", Command.SCREEN, 5),
					// this.parentCmd, this.tabbedForm );
					// UiAccess.addSubCommand( new Command("hi2 and allaf",
					// Command.SCREEN, 4), this.parentCmd, this.tabbedForm );
					// UiAccess.addSubCommand( new Command("hi3 whoey! Yes",
					// Command.SCREEN, 3), this.parentCmd, this.tabbedForm );
				}
				this.lastTabIndex = tabIndex;
			}
		}
	}

	protected String getText(int i) {
		int len = (i * 2) % 7 + (i * 3) % 13;
		StringBuffer buffer = new StringBuffer(len + 2);

		for (int j = 0; j < len; j++) {
			if (j % 3 == 0) {
				buffer.append(' ');
			} else {
				buffer.append('x');
			}
		}
		buffer.append(i);
		return buffer.toString();
	}

}