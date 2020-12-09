package com.externalalarmclock.alarmclock.resources

import org.junit.Assert
import org.junit.Test

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.externalalarmclock.pojo.ExternalAlarm

import java.time.Duration

class SetNextAlarmResourceTest {
    private val alarmStore = AlarmStore()
    private val resource = SetNextAlarmResource(alarmStore)
    private val channel = RpiWs281xChannel()
    @Test
    fun setNextAlarmTest() {
        alarmStore.wakeUpLightDuration = Duration.ofMinutes(30)
        alarmStore.addChannels(listOf(channel))
        val alarm = ExternalAlarm()
        resource.setNextAlarm(alarm)

        Assert.assertEquals(null to null, alarmStore.getNextAlarm(channel))
    }
}
