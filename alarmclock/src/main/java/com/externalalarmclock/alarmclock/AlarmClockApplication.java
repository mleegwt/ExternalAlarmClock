package com.externalalarmclock.alarmclock;

import com.externalalarmclock.alarmclock.healthchecks.AlarmClockHealthCheck;
import com.externalalarmclock.alarmclock.resources.CapabilitiesResource;
import com.externalalarmclock.alarmclock.resources.SetNextAlarmResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AlarmClockApplication extends Application<AlarmClockConfiguration> {
    public static void main(String[] args) throws Exception {
        new AlarmClockApplication().run(args);
    }

    @Override
    public String getName() {
        return "alarmclock";
    }

    @Override
    public void initialize(Bootstrap<AlarmClockConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(AlarmClockConfiguration configuration,
                    Environment environment) {
    	environment.healthChecks().register("alarmclock", new AlarmClockHealthCheck());
        environment.jersey().register(CapabilitiesResource.class);
        environment.jersey().register(SetNextAlarmResource.class);
    }

}