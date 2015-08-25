package com.zhuying.android.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间日期工具类
 */
public class DateTimeUtil {
	private static SimpleDateFormat dtf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private static int weeks = 0;

	public static String getDateStrDiff(Calendar cal, int num) {
		if (cal == null)
			cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + num);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return dtf.format(cal.getTime());
	}

	/**
	 * 年月日 时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return dtf.format(date);
	}

	/**
	 * 年月日
	 * 
	 * @param date
	 * @return
	 */
	public static String format_ymd(Date date) {
		return df.format(date);
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
	public static Date strToAllDate(String date) {
		DateFormat formaterd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formaterd.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 得到日期的前或者后几天
	 * 
	 * @param curDate
	 * @param iDate
	 *            如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
	 * @return
	 */
	public static Date getDateBeforeOrAfter(Date curDate, int iDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.DAY_OF_MONTH, iDate);
		return cal.getTime();
	}
	
	
	/**
	 * 获取今天时间
	 */
	public static String getToday() {
		Date now = new Date();
		return df.format(now);
	}
	
	/**
	 * 获取明天时间
	 */
	public static String getTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = cal.getTime();
		return df.format(tomorrow);
	}
	
	// 获得当前日期与本周日相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}
	
	/**
	 * 获得本周星期日的日期
	 */
	public static String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

//		DateFormat df = DateFormat.getDateInstance();
//		String preMonday = df.format(monday);
//		return preMonday;
		
		return df.format(monday);
	}
	
	/**
	 * 获得下周星期日的日期
	 */
	public static String getNextSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
//		DateFormat df = DateFormat.getDateInstance();
//		String preMonday = df.format(monday);
//		return preMonday;
		
		return df.format(monday);
	}
	
	/**
	 * 获得下下周星期一的日期
	 */
	public static String getNextNextMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 8 + 6);
		Date monday = currentDate.getTime();
		String preMonday = df.format(monday);
		return preMonday;
	}
}
