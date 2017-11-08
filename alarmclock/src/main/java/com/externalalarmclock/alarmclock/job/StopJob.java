package com.externalalarmclock.alarmclock.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStop;

@OnApplicationStop
public class StopJob extends Job {
	private Logger logger;
	private IRpiWs281x device;

	public StopJob(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void doJob(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Stopping application");
		device.fini();
		logger.info("Alarmclock has stopped");
	}

	public void setDevice(IRpiWs281x device) {
		this.device = device;
	}
}