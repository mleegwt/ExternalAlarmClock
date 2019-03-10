package com.externalalarmclock.lib.rpiws281x.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.madgag.gif.fmsware.GifDecoder;

@RunWith(MockitoJUnitRunner.class)
public class ImageConverterTest {
	private static final List<Color> FRAME0_COLORS = Arrays.asList(Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE,
			Color.BLACK, Color.BLUE, Color.BLACK, Color.GREEN, Color.GREEN, Color.GREEN, Color.BLACK, Color.RED,
			Color.BLACK, Color.BLACK, Color.BLACK);
	private static final List<Color> FRAME1_COLORS = Arrays.asList(Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK,
			Color.BLACK, Color.BLUE, Color.BLACK, Color.BLACK, Color.GREEN, Color.GREEN, Color.BLACK, Color.RED,
			Color.BLACK, Color.BLACK, Color.BLACK);

	private GifDecoder decoder = Mockito.mock(GifDecoder.class);

	private BufferedImage frame0 = new BufferedImage(10, 6, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage frame1 = new BufferedImage(10, 6, BufferedImage.TYPE_INT_ARGB);
	private Rectangle destination = new Rectangle(5, 3);
	private ImageConverter converter = new ImageConverter(destination, true, EStartCorner.BOTTOM_RIGHT, decoder);

	private RpiWs281xChannel channel = new RpiWs281xChannel();

	@Before
	public void setUp() {
		createFrame0();
		createFrame1();

		channel.setLedCount(15);
	}

	@Test
	public void test() {
		Mockito.when(decoder.getFrameCount()).thenReturn(2);
		Mockito.when(decoder.getFrame(Mockito.eq(0))).thenReturn(frame0);
		Mockito.when(decoder.getFrame(Mockito.eq(1))).thenReturn(frame1);

		Assert.assertEquals(2, converter.getFrames());

		assertColorsEqual(FRAME0_COLORS, converter.getBorderPixels(0, channel));
		assertColorsEqual(FRAME1_COLORS, converter.getBorderPixels(1, channel));
	}

	private void assertColorsEqual(List<Color> expected, List<Color> actual) {
		Assert.assertEquals(expected.stream().map(this::format).collect(Collectors.toList()),
				actual.stream().map(this::format).collect(Collectors.toList()));
	}

	private String format(Color color) {
		return String.format("%06X", color.getRGB() & 0xFFFFFF);
	}

	private void createFrame0() {
		setColor(frame0, 2, 7, 0, 1, Color.GREEN.getRGB());
		setColor(frame0, 2, 7, 2, 3, Color.GRAY.getRGB());
		setColor(frame0, 2, 7, 4, 5, Color.WHITE.getRGB());
		setColor(frame0, 0, 1, 2, 3, Color.BLUE.getRGB());
		setColor(frame0, 8, 9, 2, 3, Color.RED.getRGB());
	}

	private void createFrame1() {
		setColor(frame1, 3, 6, 0, 1, Color.GREEN.getRGB());
		setColor(frame1, 2, 7, 2, 3, Color.GRAY.getRGB());
		setColor(frame1, 3, 6, 4, 5, Color.WHITE.getRGB());
		setColor(frame1, 0, 1, 2, 3, Color.BLUE.getRGB());
		setColor(frame1, 8, 9, 2, 3, Color.RED.getRGB());
	}

	private void setColor(BufferedImage image, int startX, int endX, int startY, int endY, int rgb) {
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				image.setRGB(x, y, rgb);
			}
		}
	}
}

/**
 * java.lang.AssertionError: expected:<[
 * 
 * {000000}, {FFFFFF}, {FFFFFF}, {FFFFFF}, {000000}, {0000FF}, {000000},
 * {00FF00}, {00FF00}, {00FF00}, {000000}, {FF0000}
 * 
 * ]> but was:<[
 * 
 * {000000}, {FFFFFF}, {FFFFFF}, {000000}, {000000}, {0000FF}, {000000},
 * {000000}, {00FF00}, {00FF00}, {000000}, {FF0000}
 * 
 * ]>
 */
