
package com.externalalarmclock.alarmclock.resources;

import java.util.List;

import org.junit.Test;
import org.junit.Assert;

public class CapabilitiesResourceTest {
	@Test
	public void getCapabilitiesTest() {
		CapabilitiesResource res = new CapabilitiesResource();

		Assert.assertEquals(true, res.getCapabilities().isWakeupLight());
	}
}
