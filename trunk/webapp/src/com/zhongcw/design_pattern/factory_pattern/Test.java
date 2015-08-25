package com.zhongcw.design_pattern.factory_pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NameFactory factory = new NameFactory();
		/**
		 * chongwen1,zhong1
		 * zhong chongwen
		 */
		String entry = "zhong chongwen";
		Namer namer = factory.getNamer(entry);
		String first = namer.getFirst();
		String last = namer.getLast();
		System.out.println(first);
		System.out.println(last);
	}

}
