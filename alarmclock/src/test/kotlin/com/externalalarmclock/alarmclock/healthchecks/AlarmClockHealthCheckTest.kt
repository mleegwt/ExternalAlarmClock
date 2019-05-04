package com.externalalarmclock.alarmclock.healthchecks

import org.junit.Assert
import org.junit.Test

class AlarmClockHealthCheckTest {
    @Test
    fun testCheck() {
        val result = AlarmClockHealthCheck().check()

        Assert.assertTrue(result.isHealthy)
    }
}
