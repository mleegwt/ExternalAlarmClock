package com.externalalarmclock.alarmclock.healthchecks;

import org.junit.Assert;
import org.junit.Test;

import com.codahale.metrics.health.HealthCheck.Result;

public class AlarmClockHealthCheckTest {
	@Test
	public void testCheck() throws Exception {
		Result result = new AlarmClockHealthCheck().check();
		
		Assert.assertTrue(result.isHealthy());
	}
}
