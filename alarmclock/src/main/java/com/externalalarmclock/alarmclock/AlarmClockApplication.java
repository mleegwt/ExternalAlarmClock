package com.externalalarmclock.alarmclock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.externalalarmclock.alarmclock.config.AlarmClockConfiguration;
import com.externalalarmclock.alarmclock.healthchecks.AlarmClockHealthCheck;
import com.externalalarmclock.alarmclock.job.StopJob;
import com.externalalarmclock.alarmclock.job.UpdateLedsJob;
import com.externalalarmclock.alarmclock.resources.CapabilitiesResource;
import com.externalalarmclock.alarmclock.resources.SetNextAlarmResource;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;

import de.spinscale.dropwizard.jobs.JobsBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AlarmClockApplication extends Application<AlarmClockConfiguration> {
    public static void main(String[] args) throws Exception {
        new AlarmClockApplication().run(args);
    }

	private UpdateLedsJob updateLeds;
	private StopJob stopJob;
	private AlarmStore alarmStore = new AlarmStore();

    @Override
    public String getName() {
        return "alarmclock";
    }

    @Override
    public void initialize(Bootstrap<AlarmClockConfiguration> bootstrap) {
        Logger jobLogger = LoggerFactory.getLogger("jobs");
        updateLeds = new UpdateLedsJob(jobLogger, alarmStore);
        stopJob = new StopJob(jobLogger);
		bootstrap.addBundle(new JobsBundle(updateLeds, stopJob));
        bootstrap.getObjectMapper().disable(
                com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }

    @Override
    public void run(AlarmClockConfiguration configuration,
                    Environment environment) {
        IRpiWs281x device = new RpiWs281x(RpiWs281xLibrary.INSTANCE);
        device.init(5, RpiWs281xLibrary.WS2811_TARGET_FREQ, configuration.getChannels().toArray(new RpiWs281xChannel[0]));

        environment.healthChecks().register("alarmclock", new AlarmClockHealthCheck());
        environment.jersey().register(CapabilitiesResource.class);
        environment.jersey().register(new SetNextAlarmResource(alarmStore));
        
        alarmStore.addChannels(device.getChannels());
        updateLeds.setDevice(device);
        stopJob.setDevice(device);
    }
}