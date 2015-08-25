package com.zhongcw.java.util;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		// for (Object obj : list) {
		// System.out.println("list = " + obj);
		// }
		List list2 = new ArrayList();
		list2.add("eee");
		// list2.add("fff");
		// list2.add("ggg");

		// list.clear();
		list = list2;
		for (Object obj : list) {
			System.out.println(obj);
		}

	}

}
