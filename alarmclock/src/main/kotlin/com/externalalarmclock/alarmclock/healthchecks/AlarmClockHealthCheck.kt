package com.externalalarmclock.alarmclock.healthchecks

import com.codahale.metrics.health.HealthCheck

class AlarmClockHealthCheck : HealthCheck() {
    @Throws(Exception::class)
    public override fun check(): Result {
        return Result.healthy("Nothing to check yet.")
    }
}
