package com.externalalarmclock.alarmclock.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.externalalarmclock.alarmclock.AlarmStore;
import com.externalalarmclock.pojo.ExternalAlarm;

@Path("/setNextAlarm")
@Produces(MediaType.APPLICATION_JSON)
public class SetNextAlarmResource {
	private static final Logger LOG = LoggerFactory.getLogger(SetNextAlarmResource.class);
	private AlarmStore alarmStore;

	public SetNextAlarmResource(AlarmStore alarmStore) {
		this.alarmStore = alarmStore;
	}
	
	@POST
	@Timed
	public void setNextAlarm(ExternalAlarm alarm) {
		DateTime alarmTime = alarm.getAlarmTime();
		LOG.info("Next alarm set to: {}, vibrate: {}, wakeuplight: {}", alarmTime, alarm.isVibrate(),
				alarm.isWakeupLight(), alarm.getAudioStream(), alarm.getRingTone());

		alarmStore.setNextAlarm(alarmTime == null ? null : alarmTime.toGregorianCalendar().toZonedDateTime());
	}
}
