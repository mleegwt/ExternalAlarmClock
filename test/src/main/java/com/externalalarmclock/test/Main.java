package com.externalalarmclock.test;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType;
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281x;
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;

public class Main {
	private boolean clear;
	
	public Main(boolean clear) {
		this.clear = clear;
	}

	public static void main(String[] args) {
		Main main = new Main(true);
		main.showStableLeds(main::getPixelList);
	}

	public void showStableLeds(Function<RpiWs281xChannel, List<Color>> pixelSupplier) {
		IRpiWs281x dev = new RpiWs281x(RpiWs281xLibrary.INSTANCE);

		RpiWs281xChannel channel = getChannel();

		dev.init(5, RpiWs281xLibrary.WS2811_TARGET_FREQ, channel);

		Map<RpiWs281xChannel, List<Color>> pixels = getPixels(channel, clear ? this::getPixelList : pixelSupplier);
		
		dev.render(pixels);
		dev.fini();
	}

	private RpiWs281xChannel getChannel() {
		RpiWs281xChannel channel = new RpiWs281xChannel();
		channel.setBrightness((byte) 255);
		channel.setGpioNum(18);
		channel.setLedCount(300);
		channel.setStripType(ERpiWs281xStripType.SK6812_STRIP_GRBW);
		return channel;
	}

	private Map<RpiWs281xChannel, List<Color>> getPixels(RpiWs281xChannel channel, Function<RpiWs281xChannel, List<Color>> pixelSupplier) {
		List<Color> pixelsList = pixelSupplier.apply(channel);
		Map<RpiWs281xChannel, List<Color>> pixels = new HashMap<>();
		pixels.put(channel, pixelsList);
		return pixels;
	}

	private List<Color> getPixelList(RpiWs281xChannel channel) {
		return IntStream.range(0, channel.getLedCount()).mapToObj(this::getColor).collect(Collectors.toList());
	}

	private Color getColor(int pixel) {
		switch ((clear ? 4 : pixel) % 5) {
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
