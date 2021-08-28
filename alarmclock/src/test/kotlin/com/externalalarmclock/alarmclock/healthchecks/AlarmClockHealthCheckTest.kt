package com.externalalarmclock.alarmclock.healthchecks

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AlarmClockHealthCheckTest {
    @Test
    fun testCheck() {
        val result = AlarmClockHealthCheck().check()

        Assertions.assertTrue(result.isHealthy)
    }
}
