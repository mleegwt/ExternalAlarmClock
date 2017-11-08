package com.externalalarmclock.alarmclock;

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("channel")
public class ChannelFactory implements IChannelFactory {
    @JsonProperty
	private int ledCount = -1;
    @JsonProperty
	private int gpioNum = -1;
    @JsonProperty
	private byte brightness = (byte) 0xFF;
    @JsonProperty
	private ERpiWs281xStripType stripType = ERpiWs281xStripType.SK6812_STRIP_GRBW;

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