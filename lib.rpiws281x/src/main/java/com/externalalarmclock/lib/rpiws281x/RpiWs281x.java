package com.externalalarmclock.lib.rpiws281x;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;
import com.externalalarmclock.rpiws281x.ws2811_channel_t;
import com.externalalarmclock.rpiws281x.ws2811_t;
import com.sun.jna.Native;

public class RpiWs281x {
	private final RpiWs281xLibrary lib;
	private final ws2811_t strip = new ws2811_t();
	private final Map<RpiWs281xChannel, Integer> channelIndexMap = new HashMap<>();
	private int channelIndex;
	
	private boolean initialized;

	public RpiWs281x(RpiWs281xLibrary lib) {
		this.lib = lib;
	}

	public void init(int dma, int freq, RpiWs281xChannel... channels) {
		if (initialized) {
			throw new IllegalStateException("Already initialized");
		}
		channelIndex = 0;
		
		strip.dmanum = dma;
		strip.freq = freq;

		List<ws2811_channel_t> libChannels = Arrays.asList(channels).stream().map(this::getLibChannel)
				.collect(Collectors.toList());
		// Adding empty channel to make sure the end of the series is detected
		libChannels.add(new ws2811_channel_t());
		strip.channel = libChannels.toArray(new ws2811_channel_t[channels.length + 1]);

		int ret = lib.ws2811_init(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to start strip due to " + lib.ws2811_get_return_t_str(ret));
		}
		
		initialized = true;
	}

	private ws2811_channel_t getLibChannel(RpiWs281xChannel channel) {
		ws2811_channel_t libChannel = new ws2811_channel_t();
		libChannel.strip_type = channel.getStripType().getLibStripType();
		libChannel.brightness = channel.getBrightness();
		libChannel.count = channel.getLedCount();
		libChannel.gpionum = channel.getGpioNum();

		channelIndexMap.put(channel, channelIndex++);
		return libChannel;
	}
	
	public void render(Map<RpiWs281xChannel, List<Color>> pixels) {
		if (!initialized) {
			throw new IllegalStateException("Not initialized yet");
		}
		pixels.entrySet().forEach(e -> this.prepare(e.getKey(), e.getValue()));

		int ret = lib.ws2811_render(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to render strip due to " + lib.ws2811_get_return_t_str(ret));
		}
		lib.ws2811_wait(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to end rendering strip due to " + lib.ws2811_get_return_t_str(ret));
		}
	
	}
	
	private void prepare(RpiWs281xChannel channel, List<Color> pixels) {
		int intSize = Native.getNativeSize(int.class);
		int ledCount = channel.getLedCount();
		ws2811_channel_t libChannel = strip.channel[channelIndexMap.get(channel)];
		for (int i = 0; i < ledCount; i ++) {
			libChannel.leds.getPointer().getByteBuffer(i * intSize, intSize).asIntBuffer().put(pixels.get(i).getRGB());
		}
	}

	public void fini() {
		lib.ws2811_fini(strip);
		
		initialized = false;
	}
}
