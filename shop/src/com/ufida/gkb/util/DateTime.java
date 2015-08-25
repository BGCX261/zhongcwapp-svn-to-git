package com.ufida.gkb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {
	private static SimpleDateFormat dtf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public static String getDateStrDiff(Calendar cal, int num) {
		if (cal == null)
			cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + num);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return dtf.format(cal.getTime());
	}

	public static String format(Date date) {
		return dtf.format(date);
	}

	public static Calendar parse(String datetime) {
		try {
			Calendar ret = Calendar.getInstance();
			ret.setTime(dtf.parse(datetime));
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	// 获取yyyy-MM-dd是星期几。周日为0，周一为1
	public static int getWeekdayOfDateTime(String datetime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(df.parse(datetime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		return weekday;
	}

	public static int getNumberOfDaysInMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return isLeapYear(year) ? 29 : 28;
		default:
			return 0;

		}
	}

	public static boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 400 != 0);
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static String formatMinute(int c) {
		if (c >= 30)
			return "30";
		else
			return "00";
	}

	public static Calendar getDate(String datetime) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(datetime));
			return cal;
		} catch (Exception e) {
			return null;
		}
	}
}
