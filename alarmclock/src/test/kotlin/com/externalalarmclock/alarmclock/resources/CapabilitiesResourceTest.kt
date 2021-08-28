package com.externalalarmclock.alarmclock.resources

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CapabilitiesResourceTest {
    @Test
    fun getCapabilitiesTest() {
        val res = CapabilitiesResource()

        Assertions.assertEquals(true, res.capabilities.isWakeupLight)
    }
}
