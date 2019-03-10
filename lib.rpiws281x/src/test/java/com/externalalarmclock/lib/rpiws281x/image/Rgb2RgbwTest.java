package com.externalalarmclock.lib.rpiws281x.image;

import org.junit.Assert;
import org.junit.Test;

public class Rgb2RgbwTest {
	private final Rgb2Rgbw converter = new Rgb2Rgbw();

	@Test
	public void testBlack() {
		Assert.assertEquals(0, converter.getRgbw(0));
	}
	
	@Test
	public void testWhite() {
		Assert.assertEquals(0xFF000000, converter.getRgbw(0x00FFFFFF));
	}
	@Test
	public void testRedish() {
		Assert.assertEquals(0x19E60000, converter.getRgbw(0x00FF1919));
	}
}
