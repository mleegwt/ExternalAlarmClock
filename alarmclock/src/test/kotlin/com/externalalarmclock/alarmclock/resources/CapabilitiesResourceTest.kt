package com.externalalarmclock.alarmclock.resources

import org.junit.Assert
import org.junit.Test

class CapabilitiesResourceTest {
    @Test
    fun getCapabilitiesTest() {
        val res = CapabilitiesResource()

        Assert.assertEquals(true, res.capabilities.isWakeupLight)
    }
}
