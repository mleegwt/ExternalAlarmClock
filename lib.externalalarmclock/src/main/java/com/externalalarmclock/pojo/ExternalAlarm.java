package com.externalalarmclock.pojo;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalAlarm {
	@JsonProperty
	private ZonedDateTime alarmTime;
	@JsonProperty
	private boolean vibrate;
	@JsonProperty
	private boolean wakeupLight;
	@JsonProperty
	private String ringTone;
	@JsonProperty
	private String audioStream;

	public ZonedDateTime getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(ZonedDateTime alarmTime) {
		this.alarmTime = alarmTime;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public boolean isWakeupLight() {
		return wakeupLight;
	}

	public void setWakeupLight(boolean wakeupLight) {
		this.wakeupLight = wakeupLight;
	}

	public String getRingTone() {
		return ringTone;
	}

	public void setRingTone(String ringTone) {
		this.ringTone = ringTone;
	}

	public String getAudioStream() {
		return audioStream;
	}

	public void setAudioStream(String audioStream) {
		this.audioStream = audioStream;
	}
}
