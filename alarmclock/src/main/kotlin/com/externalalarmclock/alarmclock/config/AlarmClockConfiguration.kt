package com.externalalarmclock.alarmclock.config

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.jobs.JobConfiguration
import io.dropwizard.Configuration
import java.util.Collections
import javax.validation.Valid
import javax.validation.constraints.NotNull

class AlarmClockConfiguration : Configuration(), JobConfiguration {
	@JsonProperty
	@NotNull
	@Valid
	@get:JsonProperty
	@set:JsonProperty
	var channels: List<RpiWs281xChannel> = Collections.emptyList()
}
