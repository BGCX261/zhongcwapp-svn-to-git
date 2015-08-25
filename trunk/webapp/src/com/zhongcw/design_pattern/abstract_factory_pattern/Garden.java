package com.zhongcw.design_pattern.abstract_factory_pattern;

public abstract class Garden {
	public abstract Plant getCenter();

	public abstract Plant getBorder();

	public abstract Plant getShade();
}
