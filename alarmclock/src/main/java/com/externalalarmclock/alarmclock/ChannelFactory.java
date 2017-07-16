package com.externalalarmclock.alarmclock;

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("channel")
public class ChannelFactory implements IChannelFactory {
    @JsonProperty
	private int ledCount;
    @JsonProperty
	private int gpioNum;
    @JsonProperty
	private byte brightness;
    @JsonProperty
	private ERpiWs281xStripType stripType;

	@Override
	public RpiWs281xChannel createChannel() {
		RpiWs281xChannel channel = new RpiWs281xChannel();

		channel.setBrightness(brightness);
		channel.setGpioNum(gpioNum);
		channel.setLedCount(ledCount);
		channel.setStripType(stripType);
		
		return channel;
	}
}