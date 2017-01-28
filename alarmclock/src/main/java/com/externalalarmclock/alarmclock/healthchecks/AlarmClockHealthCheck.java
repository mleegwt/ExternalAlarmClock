package com.externalalarmclock.alarmclock.healthchecks;

import com.codahale.metrics.health.HealthCheck;

public class AlarmClockHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy("Nothing to check yet.");
	}
}
