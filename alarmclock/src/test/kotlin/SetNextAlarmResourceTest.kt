package com.externalalarmclock.alarmclock.resources

import java.util.Arrays

import org.junit.Assert
import org.junit.Test

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.externalalarmclock.pojo.ExternalAlarm

class SetNextAlarmResourceTest {
    private val alarmStore = AlarmStore()
    private val resource = SetNextAlarmResource(alarmStore)
    private val channel = RpiWs281xChannel()
    @Test
    fun setNextAlarmTest() {
        alarmStore.addChannels(Arrays.asList(channel))
        val alarm = ExternalAlarm()
        resource.setNextAlarm(alarm)

        Assert.assertNull(alarmStore.getNextAlarm(channel))
    }
}
