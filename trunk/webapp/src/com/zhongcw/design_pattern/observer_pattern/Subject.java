package com.zhongcw.design_pattern.observer_pattern;

public interface Subject {
	public void registerObserver(Observer obs);

	public void doBusiness();

}
