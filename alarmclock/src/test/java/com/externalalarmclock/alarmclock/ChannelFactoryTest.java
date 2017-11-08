package com.externalalarmclock.alarmclock;

import org.junit.Assert;
import org.junit.Test;

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;

public class ChannelFactoryTest {
	@Test
	public void testChannelFactory() {
		ChannelFactory factory = new ChannelFactory();
		
		RpiWs281xChannel channel = factory.createChannel();
		
		Assert.assertEquals(-1, channel.getGpioNum());
		Assert.assertEquals(-1, channel.getLedCount());
		Assert.assertEquals(-1, channel.getBrightness());
		Assert.assertEquals(ERpiWs281xStripType.SK6812_STRIP_GRBW, channel.getStripType());
	}
}
