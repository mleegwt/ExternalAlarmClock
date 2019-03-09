
package com.externalalarmclock.alarmclock.resources;

import com.externalalarmclock.alarmclock.AlarmStore;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.externalalarmclock.pojo.ExternalAlarm;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Assert;

public class SetNextAlarmResourceTest {
	private final AlarmStore alarmStore = new AlarmStore();
	private final SetNextAlarmResource resource = new SetNextAlarmResource(alarmStore);
	private final RpiWs281xChannel channel = new RpiWs281xChannel();
	@Test
	public void setNextAlarmTest() {
		alarmStore.addChannels(Arrays.asList(channel));
		ExternalAlarm alarm = new ExternalAlarm();
		resource.setNextAlarm(alarm);

		Assert.assertNull(alarmStore.getNextAlarm(channel));
	}
}
