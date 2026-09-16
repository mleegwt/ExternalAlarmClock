package com.externalalarmclock.alarmclock.config

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.jobs.JobConfiguration
import io.dropwizard.core.Configuration
import java.util.Collections
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

class AlarmClockConfiguration : Configuration(), JobConfiguration {
	@JsonProperty
	@NotNull
	@Valid
	@get:JsonProperty
	@set:JsonProperty
	var channels: List<RpiWs281xChannel> = Collections.emptyList()
}
