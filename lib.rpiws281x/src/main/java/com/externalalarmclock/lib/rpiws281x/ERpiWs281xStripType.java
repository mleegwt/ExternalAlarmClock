package com.externalalarmclock.lib.rpiws281x;

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary;

public enum ERpiWs281xStripType {
	SK6812_STRIP_GRBW(RpiWs281xLibrary.SK6812_STRIP_GRBW);
	
	private final int libStripType;

	private ERpiWs281xStripType(int libStripType) {
		this.libStripType = libStripType;
	}

	public int getLibStripType() {
		return libStripType;
	}
}
