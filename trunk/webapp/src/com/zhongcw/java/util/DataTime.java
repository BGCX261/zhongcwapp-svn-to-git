package com.zhongcw.java.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataTime {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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

	public static void main(String[] args) {
		Date date = getDateBeforeOrAfter(new Date(), -4);
		System.out.println(df.format(date));
	}
}
