package com.externalalarmclock.alarmclock

import org.junit.Assert
import org.junit.Test

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType

class ChannelFactoryTest {
    @Test
    fun testChannelFactory() {
        val factory = ChannelFactory()

        val channel = factory.createChannel()

        Assert.assertEquals(-1, channel.gpioNum.toLong())
        Assert.assertEquals(-1, channel.ledCount.toLong())
        Assert.assertEquals(-1, channel.brightness.toLong())
        Assert.assertEquals(ERpiWs281xStripType.SK6812_STRIP_GRBW, channel.stripType)
    }
}
