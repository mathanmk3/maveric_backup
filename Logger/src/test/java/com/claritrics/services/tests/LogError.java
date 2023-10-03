package com.claritrics.services.tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.claritrics.service.exceptions.ServiceException;
import com.claritrics.services.Logger;

public class LogError {

	@Test
	public void test() {
		final Logger log = Logger.getInstance();
		if (log.error("Test ERROR log messages",
				new ServiceException("EXCEPTION MESSAGE", "Test msg", null, "Caught")) <= 0)
			fail("ERROR log failed");
	}
}
