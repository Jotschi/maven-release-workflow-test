package com.gentics.tests;

import static org.junit.Assert.fail;

import org.junit.Test;

public class Simple22Test  extends AbstractTest  {

	@Test
	public void testOk() throws Exception {

	}

	@Test
	public void testFailing() throws Exception {
		fail("Dummy failure message");
	}
}
