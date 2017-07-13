package com.externalalarmclock.alarmclock.resources;

import java.awt.Color;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.externalalarmclock.pojo.ExternalAlarm;

import de.pi3g.pi.ws2812.WS2812;

@Path("/setNextAlarm")
@Produces(MediaType.APPLICATION_JSON)
public class SetNextAlarmResource {
	private static final Logger LOG = LoggerFactory.getLogger(SetNextAlarmResource.class);

	@POST
	@Timed
	public void setNextAlarm(ExternalAlarm alarm) {
		LOG.info("Next alarm set to: {}, vibrate: {}, wakeuplight: {}", alarm.getAlarmTime(), alarm.isVibrate(),
				alarm.isWakeupLight(), alarm.getAudioStream(), alarm.getRingTone());

		int length = 64;
		WS2812.get().init(length); // init a chain of 64 LEDs
		WS2812.get().clear();
		if (alarm.getAlarmTime() != null) {
			for (int i = 0; i < length; i++) {
				WS2812.get().setPixelColor(i, Color.RED);
			}
		}
		WS2812.get().show();
	}
}
