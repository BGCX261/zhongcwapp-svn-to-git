package com.zhongcw.design_pattern.observer_pattern;

import java.util.Vector;

public class SubjectImpl implements Subject {
	Vector vector;
	String name;

	public SubjectImpl() {
		vector = new Vector();
	}

	public void registerObserver(Observer obs) {
		System.out.println("被观察者注册观察者 " + ((ObserverImpl) obs).getName());
		vector.addElement(obs);
	}

	public void notifyObserver() {
		System.out.println("通知改变给所有的观察者！");
		for (int i = 0; i < vector.size(); i++) {
			Observer obs = (Observer) vector.get(i);
			obs.sendNotiy(((ObserverImpl) obs).getName());
		}
	}

	public void doBusiness() {
		System.out.println("被观察者状态改变...");
		notifyObserver();
	}

}
