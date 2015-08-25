package com.zhongcw.design_pattern.factory_pattern;

class NameFactory {
	// returns an instance of LastFirst or FirstFirst
	// depending on whether a comma is found
	public Namer getNamer(String entry) {
		int i = entry.indexOf(","); // comma determines name order
		if (i > 0)
			return new LastFirst(entry); // return one class
		else
			return new FirstFirst(entry); // or the other
	}
}
