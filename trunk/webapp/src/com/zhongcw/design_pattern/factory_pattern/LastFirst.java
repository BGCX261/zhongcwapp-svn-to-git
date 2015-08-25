package com.zhongcw.design_pattern.factory_pattern;

class LastFirst extends Namer { // split last, first
	public LastFirst(String s) {
		int i = s.indexOf(","); // find comma
		if (i > 0) {
			// left is last name
			last = s.substring(0, i).trim();
			// right is first name
			first = s.substring(i + 1).trim();
		} else {
			last = s; // put all in last name
			first = ""; // if no comma
		}
	}
}
