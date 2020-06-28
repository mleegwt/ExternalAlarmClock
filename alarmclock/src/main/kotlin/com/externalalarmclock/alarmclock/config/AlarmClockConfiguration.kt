package com.externalalarmclock.alarmclock.config

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.jobs.JobConfiguration
import io.dropwizard.Configuration
import java.util.Collections
import javax.validation.Valid
import javax.validation.constraints.NotNull

class AlarmClockConfiguration : Configuration(), JobConfiguration {
	@get:JsonProperty
	@set:JsonProperty
	var template: String = "template"
	@get:JsonProperty
	@set:JsonProperty
	var defaultName = "Stranger"
	@get:JsonProperty
	@set:JsonProperty
	var viewRendererConfiguration: Map<String, Map<String, String>> = Collections.emptyMap()
	@JsonProperty
	@NotNull
	@Valid
	@get:JsonProperty
	@set:JsonProperty
	var channels: List<RpiWs281xChannel> = Collections.emptyList()
}
