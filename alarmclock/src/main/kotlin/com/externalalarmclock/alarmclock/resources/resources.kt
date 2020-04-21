package com.externalalarmclock.alarmclock.resources

import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import org.slf4j.LoggerFactory

import com.codahale.metrics.annotation.Timed
import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.pojo.AlarmClockCapabilities
import com.externalalarmclock.pojo.ExternalAlarm
import javax.ws.rs.GET

@Path("/setNextAlarm")
@Produces(MediaType.APPLICATION_JSON)
class SetNextAlarmResource(private val alarmStore: AlarmStore) {
    @POST
    @Timed
    fun setNextAlarm(alarm: ExternalAlarm) {
        val alarmTime = alarm.alarmTime
        LOG.info("Next alarm set to: {}, vibrate: {}, wakeuplight: {}", alarmTime, alarm.isVibrate,
                alarm.isWakeupLight, alarm.audioStream, alarm.ringTone)

        val endTime = alarmTime?.toGregorianCalendar()?.toZonedDateTime()
        alarmStore.setNextAlarm(endTime)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SetNextAlarmResource::class.java)
    }
}

@Path("/capabilities")
@Produces(MediaType.APPLICATION_JSON)
class CapabilitiesResource {
    val capabilities: AlarmClockCapabilities
        @GET
        @Timed
        get() {
            val alarmClockCapabilities = AlarmClockCapabilities()
            alarmClockCapabilities.isWakeupLight = true
            return alarmClockCapabilities
        }
}
