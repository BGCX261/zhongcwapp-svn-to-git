package com.zhongcw.design_pattern.observer_pattern;

public class ObserverImpl implements Observer {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void sendNotiy(String name) {
		System.out.println("观察者 " + name + " 收到被观察者改变的通知");
	}

}
