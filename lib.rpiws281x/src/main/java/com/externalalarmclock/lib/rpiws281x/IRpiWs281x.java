package com.externalalarmclock.lib.rpiws281x;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IRpiWs281x {
	void init(int dma, int freq, RpiWs281xChannel... channels);

	void render(Map<RpiWs281xChannel, List<Color>> pixels);

	void fini();

	Collection<RpiWs281xChannel> getChannels();
}