package com.externalalarmclock.alarmclock.resources

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.externalalarmclock.pojo.ExternalAlarm
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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

        Assertions.assertEquals(null to null, alarmStore.getNextAlarm(channel))
    }
}
