package com.externalalarmclock.lib.rpiws281x.image;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;
import com.madgag.gif.fmsware.GifDecoder;

public class ImageConverter {
	private GifDecoder decoder;
	private Shape destination;
	private InputStream imageSource;
	private boolean includeCorners;
	private EStartCorner startCorner;

	public ImageConverter(Rectangle destination, boolean includeCorners, EStartCorner startCorner,
			InputStream imageSource) {
		this.destination = destination;
		this.includeCorners = includeCorners;
		this.imageSource = imageSource;
		this.startCorner = startCorner;

		decoder = new GifDecoder();
	}

	public boolean init() {
		return decoder.read(imageSource) == 0;
	}
	
	public int getFrames() {
		return decoder.getFrameCount();
	}

	public List<Color> getBorderPixels(int frame, RpiWs281xChannel channel) {
		BufferedImage image = decoder.getFrame(decoder.getFrameCount() -1);

		Rectangle2D bounds = destination.getBounds2D();
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();

		BufferedImage scaled = getScaledImage(image, width, height);

		List<Color> top = new ArrayList<>();
		List<Color> bottom = new ArrayList<>();
		List<Color> left = new ArrayList<>();
		List<Color> right = new ArrayList<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = null;
				if ((x == 0 || x == width - 1) && (y == 0 || y == height - 1)) {
					if (includeCorners) {
						color = new Color(scaled.getRGB(x, y) & 0x00FFFFFF, true);
					}
				} else {
					color = new Color(scaled.getRGB(x, y) & 0x00FFFFFF, true);
				}

				if (color == null) {
					continue;
				} else if (y == 0) {
					top.add(color);
				} else if (y == height - 1) {
					bottom.add(color);
				} else if (x == 0) {
					left.add(color);
				} else if (x == width - 1) {
					right.add(color);
				}
			}
		}

		// Assuming clockwise
		Collections.reverse(left);
		Collections.reverse(bottom);

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

		while (result.size() < channel.getLedCount()) {
			result.add(new Color(0x00000000, true));
		}
		return result;
	}


	// Poor man solution, no need for X environment.
	private BufferedImage getScaledImage(BufferedImage src, int w, int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		int x, y;
		int ww = src.getWidth();
		int hh = src.getHeight();
		for (x = 0; x < w; x++) {
			for (y = 0; y < h; y++) {
				int col = src.getRGB(x * ww / w, y * hh / h);
				img.setRGB(x, y, col & 0x00FFFFFF);
			}
		}
		return img;
	}
}
