package com.claritrics.services.tests;


import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.claritrics.services.Logger;

public class LogDebug {

	@Test
	public void test() {
		final Logger log = Logger.getInstance();
		if (log.debug("Test DEBUG log messages") <= 0)
			fail("DEBUG log failed");
	}

}
