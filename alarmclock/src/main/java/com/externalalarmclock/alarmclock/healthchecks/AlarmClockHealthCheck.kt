package com.externalalarmclock.alarmclock.healthchecks

import com.codahale.metrics.health.HealthCheck

class AlarmClockHealthCheck : HealthCheck() {

    @Throws(Exception::class)
    public override fun check(): HealthCheck.Result {
        return HealthCheck.Result.healthy("Nothing to check yet.")
    }
}
