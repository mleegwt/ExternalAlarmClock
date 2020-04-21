package com.externalalarmclock.alarmclock

import java.time.ZonedDateTime
import java.util.HashMap

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import java.time.Duration

class AlarmStore {
    lateinit var wakeUpLightDuration: Duration
    private val alarmTime = HashMap<RpiWs281xChannel, Pair<ZonedDateTime?, ZonedDateTime?>?>()

    fun getNextAlarm(channel: RpiWs281xChannel): Pair<ZonedDateTime?, ZonedDateTime?>? {
        return alarmTime[channel]
    }

    fun setNextAlarm(alarmTime: ZonedDateTime?) {
        for (channel in this.alarmTime.keys) {
            this.alarmTime[channel] = alarmTime to alarmTime?.minus(wakeUpLightDuration)
        }
    }

    fun addChannels(channels: Collection<RpiWs281xChannel>) {
        channels.forEach { c -> alarmTime[c] = null }
    }
}
