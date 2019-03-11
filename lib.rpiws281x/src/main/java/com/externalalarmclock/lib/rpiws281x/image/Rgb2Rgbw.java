package com.externalalarmclock.lib.rpiws281x.image;

/**
 * RGB to RGBW conversion.
 */
public class Rgb2Rgbw {
	/**
	 * Adapted from
	 * https://stackoverflow.com/questions/40312216/converting-rgb-to-rgbw
	 * 
	 * Using int, hex formatted as WWRRGGBB.
	 * 
	 * @param rgb
	 * @return rgbw
	 */
	public int getRgbw(int rgb) {
		int Ri = (rgb & 0x00FF0000) >> 16;
		int Gi = (rgb & 0x0000FF00) >> 8;
		int Bi = rgb & 0x000000FF;
		// Get the maximum between R, G, and B
		int tM = Math.max(Ri, Math.max(Gi, Bi));

		// If the maximum value is 0, immediately return pure black.
		if (tM == 0) {
			return 0x00000000;
		}

		// This section serves to figure out what the color with 100% hue is
		double multiplier = 255.0f / tM;
		double hR = Ri * multiplier;
		double hG = Gi * multiplier;
		double hB = Bi * multiplier;

		// This calculates the Whiteness (not strictly speaking Luminance) of the color
		double max = Math.max(hR, Math.max(hG, hB));
		double min = Math.min(hR, Math.min(hG, hB));
		double Luminance = ((max + min) / 2.0f - 127.5f) * (255.0f / 127.5f) / multiplier;

		// Calculate the output values
		// Trim them so that they are all between 0 and 255
		long Wo = fixRange((int) Luminance);
		long Bo = fixRange(Bi - Wo);
		long Ro = fixRange(Ri - Wo);
		long Go = fixRange(Gi - Wo);

		// Put it together in one integer
		return (int) ((Wo << 24) | (Ro << 16) | (Go << 8) | Bo);	}

	private long fixRange(long input) {
		if (input < 0) {
			return 0;
		} else if (input > 255) {
			return 255;
		} else {
			return input;
		}
	}
}
