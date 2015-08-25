package com.zhongcw.design_pattern.singleton_pattern;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		iSpooler pr1, pr2;
		// open one spooler--this should always work
		System.out.println("Opening one spooler");
		pr1 = iSpooler.Instance();
		if (pr1 != null)
			System.out.println("got 1 spooler");
		// try to open another spooler --should fail
		System.out.println("Opening two spoolers");
		pr2 = iSpooler.Instance();
		if (pr2 == null)
			System.out.println("no instance available");
	}
}
