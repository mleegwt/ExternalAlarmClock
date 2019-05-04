package com.externalalarmclock.pojo

import com.fasterxml.jackson.annotation.JsonProperty

import org.joda.time.DateTime

data class ExternalAlarm(
    @JsonProperty
    var alarmTime: DateTime? = null,
    @JsonProperty
    var isVibrate: Boolean = false,
    @JsonProperty
    var isWakeupLight: Boolean = false,
    @JsonProperty
    var ringTone: String? = null,
    @JsonProperty
    var audioStream: String? = null,
    @JsonProperty
    var message: String? = null
)
