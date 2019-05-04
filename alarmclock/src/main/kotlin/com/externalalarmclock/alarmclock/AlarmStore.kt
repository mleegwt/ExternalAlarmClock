package com.externalalarmclock.alarmclock

import java.time.ZonedDateTime
import java.util.HashMap

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel

class AlarmStore {
    private val alarmTime = HashMap<RpiWs281xChannel, ZonedDateTime?>()

    fun getNextAlarm(channel: RpiWs281xChannel): ZonedDateTime? {
        return alarmTime[channel]
    }

    fun setNextAlarm(alarmTime: ZonedDateTime?) {
        for (channel in this.alarmTime.keys) {
            this.alarmTime[channel] = alarmTime
        }
    }

    fun addChannels(channels: Collection<RpiWs281xChannel>) {
        channels.forEach { c -> alarmTime[c] = null }
    }
}
