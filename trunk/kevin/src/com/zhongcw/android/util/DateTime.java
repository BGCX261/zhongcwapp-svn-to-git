package com.zhongcw.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

	public static String getDateStrDiff(int num) {
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastDate = df.format(new Date(d.getTime() + num * 24 * 60 * 60
				* 1000));
		return lastDate;
	}
}
