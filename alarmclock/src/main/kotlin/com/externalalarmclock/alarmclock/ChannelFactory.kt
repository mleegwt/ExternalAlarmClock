package com.externalalarmclock.alarmclock

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import io.dropwizard.jackson.Discoverable

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
internal interface IChannelFactory : Discoverable {
    fun createChannel(): RpiWs281xChannel
}

@JsonTypeName("channel")
class ChannelFactory : IChannelFactory {
    @JsonProperty
    private val ledCount = -1
    @JsonProperty
    private val gpioNum = -1
    @JsonProperty
    private val brightness = 0xFF.toByte()
    @JsonProperty
    private val stripType = ERpiWs281xStripType.SK6812_STRIP_GRBW

    override fun createChannel(): RpiWs281xChannel {
        val channel = RpiWs281xChannel()

        channel.brightness = brightness
        channel.gpioNum = gpioNum
        channel.ledCount = ledCount
        channel.stripType = stripType

        return channel
    }
}