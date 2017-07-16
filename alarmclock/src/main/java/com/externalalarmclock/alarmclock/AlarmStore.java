package com.externalalarmclock.alarmclock;

import java.time.ZonedDateTime;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;

public class AlarmStore {
	private ZonedDateTime alarmTime;
	
	public ZonedDateTime getNextAlarm(RpiWs281xChannel channel) {
		return alarmTime;
	}

	public void setNextAlarm(ZonedDateTime alarmTime) {
		this.alarmTime = alarmTime;
	}

}
