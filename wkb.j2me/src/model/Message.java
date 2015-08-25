package model;

import java.util.Vector;

public class Message {

	private Vector cmds = new Vector();
	private Vector records = new Vector();

	public Message() {

	}

	public Vector getRecords() {
		return records;
	}

	public void setRecords(Vector records) {
		this.records = records;
	}

	public Vector getCmds() {
		return cmds;
	}

	public void addCmd(String cmd) {
		cmds.addElement(cmd);
	}

	/**
	 * 对消息进行编码，参照JMsg.pack()
	 */
	public String encode() {
		final String M_START = "<M>";
		final String M_END = "</M>";
		final String C_START = "<C>";
		final String C_END = "</C>";
		final String D_START = "<D>";
		final String D_END = "</D>";
		final String R_START = "<R>";
		final String R_END = "</R>";
		final String F_START = "<F>";
		final String F_END = "</F>";

		StringBuffer sb = new StringBuffer();
		sb.append(M_START);
		sb.append(C_START);
		for (int i = 0; i < cmds.size(); i++) {
			sb.append(cmds.elementAt(i));
			if (i != cmds.size() - 1)
				sb.append(";");
		}
		sb.append(C_END);
		sb.append(D_START);
		for (int i = 0; i < records.size(); ++i) {
			sb.append(R_START);
			Vector fields = (Vector) records.elementAt(i);
			if (fields == null) {
				sb.append(R_END);
				continue;
			}
			for (int j = 0; j < fields.size(); ++j) {
				sb.append(F_START);
				sb.append(fields.elementAt(j) == null ? "" : (String) fields
						.elementAt(j));
				sb.append(F_END);
			}
			sb.append(R_END);
		}
		sb.append(D_END);
		sb.append(M_END);
		return "$" + sb.toString() + "^";
	}

	public String getCmd(int i) {
		return cmds.elementAt(i).toString();
	}

	public static void main(String[] args) {
		Message message = new Message();
		message.addCmd("1234");
		message.addCmd("zhongcw");
		Vector recrodList = new Vector();
		Vector fieldList = new Vector();
		fieldList.addElement("a" + ";" + "b" + ";" + "c" + ";");

		Vector fieldList2 = new Vector();
		fieldList2.addElement("x" + ";" + "y" + ";" + "z" + ";");

		recrodList.addElement(fieldList);
		recrodList.addElement(fieldList2);
		message.setRecords(recrodList);

		System.out.println(message.encode());
	}
}
