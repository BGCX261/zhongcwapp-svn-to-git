package com.zhuying.android.util;

import java.util.Comparator;

import android.content.ContentValues;

/**
 * 拼音排序
 */
public class PinYinComparator implements Comparator {
	private String sortKey = "sort_key";

	@Override
	public int compare(Object o1, Object o2) {
		ContentValues u1 = (ContentValues) o1;
		ContentValues u2 = (ContentValues) o2;
		String name1 = u1.getAsString(sortKey);
		String name2 = u2.getAsString(sortKey);
		int flag = name1.compareTo(name2);
		return flag;
	}
}
