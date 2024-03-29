package com.externalalarmclock.alarmclock

import com.externalalarmclock.alarmclock.config.AlarmClockConfiguration
import com.externalalarmclock.alarmclock.healthchecks.AlarmClockHealthCheck
import com.externalalarmclock.alarmclock.job.StopJob
import com.externalalarmclock.alarmclock.job.UpdateLedsJob
import com.externalalarmclock.alarmclock.resources.CapabilitiesResource
import com.externalalarmclock.alarmclock.resources.SetNextAlarmResource
import com.externalalarmclock.lib.rpiws281x.RpiWs281x
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary
import io.dropwizard.jobs.JobsBundle
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.slf4j.LoggerFactory
import java.time.Duration

fun main(vararg args: String) {
	AlarmClockApplication().run(*args)
}

class AlarmClockApplication : Application<AlarmClockConfiguration>() {
	private val jobLogger = LoggerFactory.getLogger("jobs")
	private val alarmStore = AlarmStore()
	private val device = RpiWs281x(RpiWs281xLibrary.INSTANCE)
	private val updateLeds = UpdateLedsJob(jobLogger, alarmStore, device)
	private val stopJob = StopJob(jobLogger, device)

	override fun initialize(bootstrap: Bootstrap<AlarmClockConfiguration>) {
		bootstrap.addBundle(JobsBundle(updateLeds, stopJob))
		bootstrap.objectMapper.disable(
			com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS
		)
	}

	override fun run(
		configuration: AlarmClockConfiguration,
		environment: Environment
	) {
		device.init(
			5,
			RpiWs281xLibrary.WS2811_TARGET_FREQ,
			*configuration.channels.toTypedArray()
		)
		environment.healthChecks().register("alarmclock", AlarmClockHealthCheck())
		environment.jersey().register(CapabilitiesResource::class.java)
		environment.jersey().register(SetNextAlarmResource(alarmStore))
		alarmStore.addChannels(device.channels)
		alarmStore.wakeUpLightDuration = Duration.ofSeconds(30)
	}
}
