package com.externalalarmclock.alarmclock

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ChannelFactoryTest {
    @Test
    fun testChannelFactory() {
        val factory = ChannelFactory()

        val channel = factory.createChannel()

        Assertions.assertEquals(-1, channel.gpioNum.toLong())
        Assertions.assertEquals(-1, channel.ledCount.toLong())
        Assertions.assertEquals(-1, channel.brightness.toLong())
        Assertions.assertEquals(ERpiWs281xStripType.SK6812_STRIP_GRBW, channel.stripType)
    }
}
