package com.externalalarmclock.test;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType;
import com.externalalarmclock.lib.rpiws281x.RpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;

public class Main {

	public static void main(String[] args) {
		RpiWs281x dev = new RpiWs281x(RpiWs281xLibrary.INSTANCE);

		RpiWs281xChannel channel = new RpiWs281xChannel();
		channel.setBrightness((byte) 255);
		channel.setGpioNum(18);
		int ledCount = 64;
		channel.setLedCount(ledCount);
		channel.setStripType(ERpiWs281xStripType.SK6812_STRIP_GRBW);

		dev.init(5, RpiWs281xLibrary.WS2811_TARGET_FREQ, channel);

		Map<RpiWs281xChannel, List<Color>> pixels = getPixels(channel, ledCount);
		
		dev.render(pixels);
		dev.fini();
	}

	private static Map<RpiWs281xChannel, List<Color>> getPixels(RpiWs281xChannel channel, int ledCount) {
		List<Color> pixelsList = IntStream.range(0, ledCount).mapToObj(Main::getColor).collect(Collectors.toList());
		Map<RpiWs281xChannel, List<Color>> pixels = new HashMap<>();
		pixels.put(channel, pixelsList);
		return pixels;
	}

	private static Color getColor(int pixel) {
		switch (pixel % 5) {
		case 0:
			return new Color(0x00FF0000, true);
		case 1:
			return new Color(0x0000FF00, true);
		case 2:
			return new Color(0x000000FF, true);
		case 3:
			return new Color(0xFF000000, true);
		default:
			return new Color(0x00000000, true);
		}
	}

}
