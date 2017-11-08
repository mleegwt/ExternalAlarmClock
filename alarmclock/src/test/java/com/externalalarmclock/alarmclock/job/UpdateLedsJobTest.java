package com.externalalarmclock.alarmclock.job;

import java.awt.Color;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

import com.externalalarmclock.alarmclock.AlarmStore;
import com.externalalarmclock.alarmclock.job.UpdateLedsJob;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;

public class UpdateLedsJobTest {
	private static final int LED_COUNT = 300;
	private final AlarmStore alarmStore = new AlarmStore();
	private final UpdateLedsJob sut = new UpdateLedsJob(LoggerFactory.getLogger(UpdateLedsJob.class), alarmStore);
	private final IRpiWs281x device = Mockito.mock(IRpiWs281x.class);
	private final RpiWs281xChannel channel = new RpiWs281xChannel();

	@Before
	public void setUp() {
		sut.setDevice(device);

		channel.setLedCount(LED_COUNT);

		List<RpiWs281xChannel> channels = Arrays.asList(channel);
		alarmStore.addChannels(channels);
		Mockito.when(device.getChannels()).thenReturn(channels);
	}

	@Test
	public void testUpdateLedsNoAlarm() throws JobExecutionException {
		alarmStore.setNextAlarm(null);

		sut.doJob(null);

		Mockito.verify(device).getChannels();
		Mockito.verify(device, Mockito.never()).render(Mockito.any());
		
		Assert.assertEquals(0, sut.getFrame());
	}

	@Test
	public void testUpdateLedsFutureAlarmNearby() throws JobExecutionException {
		alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)));

		sut.doJob(null);

		Mockito.verify(device).getChannels();
		Mockito.verify(device).render(Mockito.any());

		Assert.assertEquals(20, sut.getFrame());
	}
	
	@Test
	public void testUpdateLedsCleared() throws JobExecutionException {
		alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)));
		sut.doJob(null);
		alarmStore.setNextAlarm(null);
		sut.doJob(null);

		Mockito.verify(device, Mockito.times(2)).getChannels();
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<RpiWs281xChannel, List<Color>>> argument = ArgumentCaptor.forClass(Map.class);
		Mockito.verify(device, Mockito.times(2)).render(argument.capture());

		Map<RpiWs281xChannel, List<Color>> renderMap = argument.getValue();
		Assert.assertEquals(1, renderMap.size());
		Assert.assertEquals(LED_COUNT, renderMap.get(channel).stream().filter(c -> c.getRGB() == 0).count());
		Assert.assertEquals(0, sut.getFrame());
	}
}
