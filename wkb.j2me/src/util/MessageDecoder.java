package util;

import gov.nist.core.StringTokenizer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import model.Message;

public class MessageDecoder {

	public static Message decode(String raw) {
		Message ret = new Message();
		int cmdS = raw.indexOf("<C>");
		int cmdE = raw.indexOf("</C>");
		StringTokenizer st = new StringTokenizer(raw.substring(cmdS + 3, cmdE),
				';');
		String value = null;
		while (st.hasMoreChars()) {
			value = st.nextToken();
			ret.addCmd(value.replace(';', ' ').trim());
		}

		// TODO get <D>
		int dataS = raw.indexOf("<D>");
		int dataE = raw.indexOf("</D>");
		String data = raw.substring(dataS, dataE + 4);
		// System.out.println(data);
		parserXML(data, ret);
		return ret;
	}

	public static void parserXML(String data, Message ret) {
		KXmlParser parser = new KXmlParser();
		try {
			parser.setInput(new InputStreamReader(new ByteArrayInputStream(data
					.getBytes()), "utf-8"));
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "D");
			Vector records = new Vector();
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, "R");

				Vector fields = new Vector();
				while (parser.nextTag() != XmlPullParser.END_TAG) {
					String name = parser.getName();
					String text = parser.nextText();
					fields.addElement(text);
					// System.out.println("<" + name + ">: " + text);
					parser.require(XmlPullParser.END_TAG, null, name);
				}
				records.addElement(fields);
				parser.require(XmlPullParser.END_TAG, null, "R");
			}

			parser.require(XmlPullParser.END_TAG, null, "D");
			parser.next();
			parser.require(XmlPullParser.END_DOCUMENT, null, null);

			ret.setRecords(records);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Message msg = MessageDecoder
				.decode("<M><C>9002;1;244</C><D><R><F>SY7798;100031;N;zn_test130;</F></R><R><F>AAAAAA;987654;Y;wkbtest;</F></R></D></M>");
		Vector cmds = msg.getCmds();
		for (int i = 0; i < cmds.size(); i++) {
			// System.out.println(cmds.elementAt(i));
		}
		Vector records = msg.getRecords();
		for (int i = 0; i < records.size(); i++) {
			Vector fields = (Vector) records.elementAt(i);
			System.out.println(fields.elementAt(0));
		}

	}
}
