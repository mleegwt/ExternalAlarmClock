package com.externalalarmclock.lib.rpiws281x;

public class RpiWs281xChannel {
	private int ledCount;
	private int gpioNum;
	private byte brightness;
	private ERpiWs281xStripType stripType;

	public int getLedCount() {
		return ledCount;
	}

	public void setLedCount(int ledCount) {
		this.ledCount = ledCount;
	}

	public int getGpioNum() {
		return gpioNum;
	}

	public void setGpioNum(int gpioNum) {
		this.gpioNum = gpioNum;
	}

	public byte getBrightness() {
		return brightness;
	}

	public void setBrightness(byte brightness) {
		this.brightness = brightness;
	}

	public ERpiWs281xStripType getStripType() {
		return stripType;
	}

	public void setStripType(ERpiWs281xStripType stripType) {
		this.stripType = stripType;
	}
}
