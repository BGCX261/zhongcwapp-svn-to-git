package im;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class SocketMIDlet extends MIDlet implements CommandListener {
	private boolean isPaused;
	private LoginWindow loginWindow;

	public SocketMIDlet() {
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void startApp() {
		isPaused = false;
		loginWindow = new LoginWindow(this);
	}

	public void pauseApp() {
		isPaused = true;
	}

	public void destroyApp(boolean unconditional) {
	}

	public void commandAction(Command c, Displayable s) {
	}
}
