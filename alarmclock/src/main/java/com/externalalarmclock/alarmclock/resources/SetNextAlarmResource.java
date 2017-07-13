package com.externalalarmclock.alarmclock.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.externalalarmclock.pojo.ExternalAlarm;
import com.externalalarmclock.test.Main;

@Path("/setNextAlarm")
@Produces(MediaType.APPLICATION_JSON)
public class SetNextAlarmResource {
	private static final Logger LOG = LoggerFactory.getLogger(SetNextAlarmResource.class);

	@POST
	@Timed
	public void setNextAlarm(ExternalAlarm alarm) {
		LOG.info("Next alarm set to: {}, vibrate: {}, wakeuplight: {}", alarm.getAlarmTime(), alarm.isVibrate(),
				alarm.isWakeupLight(), alarm.getAudioStream(), alarm.getRingTone());

		new Main(alarm.getAlarmTime() == null).showStableLeds();;
	}
}
