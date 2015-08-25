package im;

public class Log {
	public static void info(String msg) {
		System.out.println("[info]:" + msg);
	}

	public static void error(String msg) {
		System.out.println("[error]:" + msg);
	}

	public static void error(Exception e) {
		e.printStackTrace();
		System.out.println("[error]:" + e.getMessage());
	}

	public static void debugMsg(String msg) {
		byte[] bytes = msg.getBytes();
		for (int i = 0; i < msg.length(); i++) {
			if (i % 16 == 0)
				System.out.println();
			System.out.print(bytes[i] + "\t");

		}
	}
}
