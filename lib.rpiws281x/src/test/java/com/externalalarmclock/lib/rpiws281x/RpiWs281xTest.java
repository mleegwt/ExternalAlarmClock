package com.externalalarmclock.lib.rpiws281x;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary.ws2811_device;
import com.externalalarmclock.rpiws281x.ws2811_channel_t;
import com.externalalarmclock.rpiws281x.ws2811_t;
import com.sun.jna.Memory;
import com.sun.jna.Native;

@RunWith(MockitoJUnitRunner.class)
public class RpiWs281xTest {
	@Mock
	RpiWs281xLibrary lib;

	@InjectMocks
	RpiWs281x wrapper;

	RpiWs281xChannel channel = new RpiWs281xChannel();

	@Before
	public void setUp() {
		channel.setStripType(ERpiWs281xStripType.SK6812_STRIP_GRBW);
		channel.setBrightness((byte) 0xFF);
		channel.setGpioNum(18);
		channel.setLedCount(10);
	}

	@Test
	public void testGamma() {
		Assert.assertEquals(256, RpiWs281x.GAMMA.length);
	}

	@Test(expected = IllegalStateException.class)
	public void initFailedTest() {
		wrapper.init(1, 1, channel);
		wrapper.init(1, 1, channel);
	}

	@Test(expected = IllegalStateException.class)
	public void initFailedInitTest() {
		Mockito.when(lib.ws2811_init(Mockito.any())).thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_DMA);

		try {
			wrapper.init(1, 1, channel);
		} finally {
			Mockito.verify(lib).ws2811_init(Mockito.any());
		}
	}

	private Integer initLib(InvocationOnMock inv) {
		ws2811_t arg = inv.getArgument(0);
		arg.device = new ws2811_device();
		long intSize = Native.getNativeSize(int.class);
		for (int i = 0; i < arg.channel.length - 1; i++) {
			Memory mem = new Memory((channel.getLedCount() + 1) * intSize);
			ws2811_channel_t channel = arg.channel[i];
			channel.leds.setPointer(mem.getPointer(0));
		}
		return RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS;

	}

	@Test
	public void happyFLowTest() {
		Mockito.when(lib.ws2811_init(Mockito.any())).thenAnswer(this::initLib);
		wrapper.init(1, 1, channel);

		Assert.assertArrayEquals(new Object[] { channel }, wrapper.getChannels().toArray());

		Mockito.verify(lib).ws2811_init(Mockito.any());

		Map<RpiWs281xChannel, List<Color>> pixels = new HashMap<>();
		List<Color> colors = Arrays.asList(Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.WHITE,
				Color.GRAY, Color.PINK, Color.CYAN, Color.MAGENTA);
		pixels.put(channel, colors);
		wrapper.render(pixels);

		Mockito.verify(lib).ws2811_render(Mockito.any());
		Mockito.verify(lib).ws2811_wait(Mockito.any());

		wrapper.fini();

		Mockito.verify(lib).ws2811_fini(Mockito.any());

		// Verifying that we can restart now.
		wrapper.init(1, 1, channel);
	}

	@Test(expected = IllegalStateException.class)
	public void renderNotInitializedTest() {
		wrapper.render(null);
	}

	@Test(expected = IllegalStateException.class)
	public void renderFailedTest() {
		Mockito.when(lib.ws2811_render(Mockito.any()))
				.thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_GENERIC);
		wrapper.init(1, 1, channel);
		wrapper.render(new HashMap<>());
	}

	@Test(expected = IllegalStateException.class)
	public void renderWaitFailedTest() {
		Mockito.when(lib.ws2811_wait(Mockito.any())).thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_GENERIC);
		wrapper.init(1, 1, channel);
		wrapper.render(new HashMap<>());
	}
}
