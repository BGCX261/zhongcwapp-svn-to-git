package com.zhongcw.design_pattern.singleton_pattern;

import junit.framework.TestCase;

public class SingletonPatternDemoTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetInstance() {
		// fail("Not yet implemented");
		SingletonPatternDemo s1, s2;
		s1 = SingletonPatternDemo.getInstance();
		s2 = SingletonPatternDemo.getInstance();
		assertEquals(true, s1.equals(s2));
	}

}
