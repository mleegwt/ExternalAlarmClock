package com.externalalarmclock.lib.rpiws281x;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;
import com.externalalarmclock.rpiws281x.ws2811_channel_t;
import com.externalalarmclock.rpiws281x.ws2811_t;
import com.sun.jna.Native;

@SuppressWarnings("squid:S1191")
public class RpiWs281x implements IRpiWs281x {
	static final int[] GAMMA = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
			1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10,
			10, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 21, 21, 22, 22, 23,
			23, 24, 25, 25, 26, 27, 27, 28, 29, 29, 30, 31, 31, 32, 33, 34, 34, 35, 36, 37, 37, 38, 39, 40, 40, 41, 42,
			43, 44, 45, 46, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68,
			69, 70, 71, 72, 73, 74, 76, 77, 78, 79, 80, 81, 83, 84, 85, 86, 88, 89, 90, 91, 93, 94, 95, 96, 98, 99, 100,
			102, 103, 104, 106, 107, 109, 110, 111, 113, 114, 116, 117, 119, 120, 121, 123, 124, 126, 128, 129, 131,
			132, 134, 135, 137, 138, 140, 142, 143, 145, 146, 148, 150, 151, 153, 155, 157, 158, 160, 162, 163, 165,
			167, 169, 170, 172, 174, 176, 178, 179, 181, 183, 185, 187, 189, 191, 193, 194, 196, 198, 200, 202, 204,
			206, 208, 210, 212, 214, 216, 218, 220, 222, 224, 227, 229, 231, 233, 235, 237, 239, 241, 244, 246, 248,
			250, 252, 255 };
	private final RpiWs281xLibrary lib;
	private final ws2811_t strip = new ws2811_t();
	private final Map<RpiWs281xChannel, Integer> channelIndexMap = new HashMap<>();
	private int channelIndex;

	private boolean initialized;

	public RpiWs281x(RpiWs281xLibrary lib) {
		this.lib = lib;
	}

	@Override
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

	@Override
	public void render(Map<RpiWs281xChannel, List<Color>> pixels) {
		if (!initialized) {
			throw new IllegalStateException("Not initialized yet");
		}
		pixels.entrySet().forEach(e -> this.prepare(e.getKey(), e.getValue()));

		int ret = lib.ws2811_render(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to render strip due to " + lib.ws2811_get_return_t_str(ret));
		}
		ret = lib.ws2811_wait(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to end rendering strip due to " + lib.ws2811_get_return_t_str(ret));
		}

	}

	private void prepare(RpiWs281xChannel channel, List<Color> pixels) {
		long intSize = Native.getNativeSize(int.class);
		int ledCount = channel.getLedCount();
		ws2811_channel_t libChannel = strip.channel[channelIndexMap.get(channel)];
		for (int i = 0; i < ledCount; i++) {
			ByteBuffer byteBuffer = libChannel.leds.getPointer().getByteBuffer(i * intSize, intSize);
			byteBuffer.asIntBuffer().put(corectGamma(pixels.get(i).getRGB()));

		}
	}

	private int corectGamma(int rgb) {
		byte correctedW = (byte) GAMMA[(rgb & 0xFF000000) >> 24];
		byte correctedR = (byte) GAMMA[(rgb & 0x00FF0000) >> 16];
		byte correctedG = (byte) GAMMA[(rgb & 0x0000FF00) >> 8];
		byte correctedB = (byte) GAMMA[rgb & 0x000000FF];

		return ((correctedW << 24) & 0xFF000000) | ((correctedR << 16) & 0x00FF0000) | ((correctedG << 8) & 0x0000FF00)
				| (correctedB & 0x000000FF);
	}

	@Override
	public void fini() {
		lib.ws2811_fini(strip);

		initialized = false;
	}

	@Override
	public Collection<RpiWs281xChannel> getChannels() {
		return channelIndexMap.keySet();
	}
}
