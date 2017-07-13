package com.externalalarmclock.test;

import java.awt.Color;

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;
import com.externalalarmclock.rpiws281x.ws2811_channel_t;
import com.externalalarmclock.rpiws281x.ws2811_t;
import com.sun.jna.Native;

public class Main {

	public static void main(String[] args) {
		int length = 64;

		RpiWs281xLibrary lib = RpiWs281xLibrary.INSTANCE;
		ws2811_t strip = new ws2811_t();
		strip.channel = new ws2811_channel_t[2];
		strip.dmanum = 5;
		strip.freq = RpiWs281xLibrary.WS2811_TARGET_FREQ;

		ws2811_channel_t first = new ws2811_channel_t();
		first.strip_type = RpiWs281xLibrary.SK6812_STRIP_GRBW;
		first.brightness = (byte) 255;
		first.count = length;
		first.gpionum = 18;

		strip.channel[0] = first;

		ws2811_channel_t second = new ws2811_channel_t();
		strip.channel[1] = second;

		int ret = lib.ws2811_init(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to start strip due to " + lib.ws2811_get_return_t_str(ret));
		}

		int intSize = Native.getNativeSize(int.class);
		for (int i = 0; i < length * intSize; i += intSize) {
			first.leds.getPointer().getByteBuffer(i, intSize).asIntBuffer().put(getColor(i / 4).getRGB());
		}

		ret = lib.ws2811_render(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to render strip due to " + lib.ws2811_get_return_t_str(ret));
		}
		lib.ws2811_wait(strip);
		if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
			throw new IllegalStateException("Failed to end rendering strip due to " + lib.ws2811_get_return_t_str(ret));
		}
		lib.ws2811_fini(strip);
	}

	private static Color getColor(int pixel) {
		switch (pixel % 4) {
		case 0:
			return new Color(0x00FF0000, true);
		case 1:
			return new Color(0x0000FF00, true);
		case 2:
			return new Color(0x000000FF, true);
		default:
			return new Color(0xFF000000, true);
		}
	}

}
