package com.zhongcw.design_pattern.observer_pattern;

public class Test {

	public static void main(String[] args) {
		Subject subject = new SubjectImpl();
		ObserverImpl observer1 = new ObserverImpl();
		observer1.setName("张三");
		ObserverImpl observer2 = new ObserverImpl();
		observer2.setName("李四");
		subject.registerObserver(observer1);
		subject.registerObserver(observer2);

		subject.doBusiness();
	}
}
