package com.externalalarmclock.pojo

import java.util.ArrayList

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Stores alarm clock capabilities.
 */
data class AlarmClockCapabilities(
    @JsonProperty
    var isVibrate: Boolean = false,
    @JsonProperty
    var isWakeupLight: Boolean = false,
    @JsonProperty
    var ringTone: List<String> = ArrayList(),
    @JsonProperty
    var isStreamAudio: Boolean = false)
