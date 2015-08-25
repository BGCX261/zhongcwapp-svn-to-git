package com.zhongcw.design_pattern.abstract_factory_pattern;

public class Plant {
	String name;

	public Plant(String pname) {
		name = pname; // save name
	}

	public String getName() {
		return name;
	}
}
