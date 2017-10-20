package com.externalalarmclock.alarmclock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

import com.externalalarmclock.alarmclock.job.UpdateLedsJob;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;

public class UpdateLedsJobTest {
	private final AlarmStore alarmStore = new AlarmStore();
	private final UpdateLedsJob sut = new UpdateLedsJob(LoggerFactory.getLogger(UpdateLedsJob.class), alarmStore);
	private final IRpiWs281x device = Mockito.mock(IRpiWs281x.class);

	@Before
	public void setUp() {
		sut.setDevice(device);

		RpiWs281xChannel channel = new RpiWs281xChannel();
		Mockito.when(device.getChannels()).thenReturn(Arrays.asList(channel));
	}

	@Test
	public void testUpdateLedsNoAlarm() throws JobExecutionException {
		alarmStore.setNextAlarm(null);

		sut.doJob(null);

		Mockito.verify(device).getChannels();
		Mockito.verify(device, Mockito.never()).render(Mockito.any());
	}
}
