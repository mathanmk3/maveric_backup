package com.claritrics.services.tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.claritrics.services.Logger;

public class LogActivity {

	@Test
	public void test() {
		final Logger log = Logger.getInstance();
		final int activityId = log.activity("1", "User", "Test activity", "Test");
		if (activityId <= 0)
			fail("Activity entry failed - " + activityId);
	}

}
