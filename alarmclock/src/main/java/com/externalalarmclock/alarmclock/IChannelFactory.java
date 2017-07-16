package com.externalalarmclock.alarmclock;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
interface IChannelFactory extends Discoverable {
    RpiWs281xChannel createChannel();
}