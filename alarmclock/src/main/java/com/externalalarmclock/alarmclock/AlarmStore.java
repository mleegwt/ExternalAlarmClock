package com.externalalarmclock.alarmclock;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel;

public class AlarmStore {
	private Map<RpiWs281xChannel, ZonedDateTime> alarmTime = new HashMap<>();

	public ZonedDateTime getNextAlarm(RpiWs281xChannel channel) {
		return alarmTime.get(channel);
	}

	public void setNextAlarm(ZonedDateTime alarmTime) {
		for (RpiWs281xChannel channel : this.alarmTime.keySet()) {
			this.alarmTime.put(channel, alarmTime);
		}
	}

	public void addChannels(Collection<RpiWs281xChannel> channels) {
		channels.forEach(c -> alarmTime.put(c, null));
	}
}
