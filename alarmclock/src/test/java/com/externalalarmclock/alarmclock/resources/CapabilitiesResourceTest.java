
package com.externalalarmclock.alarmclock.resources;

import org.junit.Assert;
import org.junit.Test;

public class CapabilitiesResourceTest {
	@Test
	public void getCapabilitiesTest() {
		CapabilitiesResource res = new CapabilitiesResource();

		Assert.assertEquals(true, res.getCapabilities().isWakeupLight());
	}
}
