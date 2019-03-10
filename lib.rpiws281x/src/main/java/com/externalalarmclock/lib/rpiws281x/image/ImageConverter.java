package com.externalalarmclock.lib.rpiws281x.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.madgag.gif.fmsware.GifDecoder;

public class ImageConverter {
	private GifDecoder decoder;
	private Shape destination;
	private boolean includeCorners;
	private EStartCorner startCorner;
	private int width;
	private int height;

	public ImageConverter(Rectangle destination, boolean includeCorners, EStartCorner startCorner,
			GifDecoder decoder) {
		this.destination = destination;
		this.includeCorners = includeCorners;
		this.startCorner = startCorner;
		this.decoder = decoder;
	}

	public int getFrames() {
		return decoder.getFrameCount();
	}

	public List<Color> getBorderPixels(int frame, RpiWs281xChannel channel) {
		BufferedImage image = decoder.getFrame(frame);

		Rectangle2D bounds = destination.getBounds2D();
		width = (int) bounds.getWidth();
		height = (int) bounds.getHeight();

		BufferedImage scaled = getScaledImage(image);

		List<Color> top = new ArrayList<>();
		List<Color> bottom = new ArrayList<>();
		List<Color> left = new ArrayList<>();
		List<Color> right = new ArrayList<>();
		getImageBorders(scaled, top, bottom, left, right);

		// Assuming clockwise
		Collections.reverse(left);
		Collections.reverse(bottom);

		List<Color> result = createLedStripContents(top, bottom, left, right);

		while (result.size() < channel.getLedCount()) {
			result.add(new Color(0x00000000, true));
		}
		return result;
	}

	private void getImageBorders(BufferedImage scaled, List<Color> top, List<Color> bottom, List<Color> left,
			List<Color> right) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = getPixelColor(scaled, x, y);

				if (color == null) {
					continue;
				}

				addColorToBorder(top, bottom, left, right, x, y, color);
			}
		}
	}

	private void addColorToBorder(List<Color> top, List<Color> bottom, List<Color> left, List<Color> right, int x,
			int y, Color color) {
		if (y == 0) {
			top.add(color);
		} else if (y == height - 1) {
			bottom.add(color);
		} else if (x == 0) {
			left.add(color);
		} else if (x == width - 1) {
			right.add(color);
		}
	}

	private Color getPixelColor(BufferedImage scaled, int x, int y) {
		Color color = null;
		if ((x == 0 || x == width - 1) && (y == 0 || y == height - 1)) {
			if (includeCorners) {
				color = new Color(scaled.getRGB(x, y) & 0x00FFFFFF, true);
			}
		} else {
			color = new Color(scaled.getRGB(x, y) & 0x00FFFFFF, true);
		}
		return color;
	}

	private List<Color> createLedStripContents(List<Color> top, List<Color> bottom, List<Color> left,
			List<Color> right) {
		List<Color> result = new ArrayList<>();
		switch (startCorner) {
		case BOTTOM_LEFT:
			result.addAll(left);
			result.addAll(top);
			result.addAll(right);
			result.addAll(bottom);
			break;
		case BOTTOM_RIGHT:
			result.addAll(bottom);
			result.addAll(left);
			result.addAll(top);
			result.addAll(right);
			break;
		case TOP_LEFT:
			result.addAll(top);
			result.addAll(right);
			result.addAll(bottom);
			result.addAll(left);
			break;
		case TOP_RIGHT:
			result.addAll(right);
			result.addAll(bottom);
			result.addAll(left);
			result.addAll(top);
			break;
		default:
			throw new IllegalArgumentException("Unexpected start corder " + startCorner);
		}
		return result;
	}

	// Poor man solution, no need for X environment.
	private BufferedImage getScaledImage(BufferedImage src) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int ww = src.getWidth();
		int hh = src.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int col = src.getRGB(x * ww / width, y * hh / height);
				img.setRGB(x, y, col & 0x00FFFFFF);
			}
		}
		return img;
	}
}
