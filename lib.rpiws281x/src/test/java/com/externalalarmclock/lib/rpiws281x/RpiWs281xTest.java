package com.externalalarmclock.lib.rpiws281x;

import org.junit.Assert;
import org.junit.Test;

import com.externalalarmclock.lib.rpiws281x.RpiWs281x;

public class RpiWs281xTest {
	@Test
	public void testGamma() {
		Assert.assertEquals(256, RpiWs281x.GAMMA.length);
	}
}
