package com.zhongcw.design_pattern.singleton_pattern;

/**
 * 单例模式
 */
public class SingletonPatternDemo {

	private static SingletonPatternDemo instance; // 该类实例（静态）

	/**
	 * 构造器（私有）
	 */
	private SingletonPatternDemo() {

	}

	/**
	 * 获取该类唯一实例（静态方法）
	 * 
	 * @return
	 */
	public static SingletonPatternDemo getInstance() {
		if (instance == null) { // 如果该类不存在实例，则创建
			instance = new SingletonPatternDemo();
		} else { // 如果该类存在实例，则返回
		}
		return instance;
	}
}
