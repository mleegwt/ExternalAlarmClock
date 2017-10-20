package com.externalalarmclock.alarmclock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

import com.externalalarmclock.alarmclock.job.StopJob;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;

public class StopJobTest {
	private final StopJob sut = new StopJob(LoggerFactory.getLogger(StopJob.class));
	private final IRpiWs281x device = Mockito.mock(IRpiWs281x.class);

	@Before
	public void setUp() {
		sut.setDevice(device);
	}

	@Test
	public void testCloseDevice() throws JobExecutionException {
		sut.doJob(null);

		Mockito.verify(device).fini();
	}
}
