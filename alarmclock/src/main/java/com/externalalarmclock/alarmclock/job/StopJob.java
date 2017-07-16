package com.externalalarmclock.alarmclock.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.externalalarmclock.lib.rpiws281x.RpiWs281x;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStop;

@OnApplicationStop
public class StopJob extends Job {
	private Logger logger;
	private RpiWs281x device;

	public StopJob(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void doJob(JobExecutionContext context) throws JobExecutionException {
		device.fini();
	}

	public void setDevice(RpiWs281x device) {
		this.device = device;
	}
}