package com.zhongcw.unit_test;

import junit.framework.TestCase;

public class MyUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetNumber() {
		// fail("Not yet implemented");
		MyUnit myUnit = new MyUnit();
		assertEquals(4, myUnit.plusNumber(1, 2));
	}

}
