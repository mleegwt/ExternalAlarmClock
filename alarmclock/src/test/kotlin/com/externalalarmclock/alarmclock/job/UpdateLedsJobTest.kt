package com.externalalarmclock.alarmclock.job

import java.awt.Color
import java.time.Duration
import java.time.ZonedDateTime

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.nhaarman.mockitokotlin2.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.quartz.JobExecutionContext

@RunWith(MockitoJUnitRunner::class)
class UpdateLedsJobTest {
    private val alarmStore = AlarmStore()
    private val sut = UpdateLedsJob(LoggerFactory.getLogger(UpdateLedsJob::class.java), alarmStore)
    private val channel = RpiWs281xChannel(ledCount = LED_COUNT)
    private val device = mock<IRpiWs281x> {
        on { channels } doReturn listOf(channel)
    }
    private val context = mock<JobExecutionContext> {}
    private val argument = argumentCaptor<(Map<RpiWs281xChannel, List<Color>>)>()

    @Before
    fun setUp() {
        sut.setDevice(device)

        val channels = listOf(channel)
        alarmStore.addChannels(channels)
        alarmStore.wakeUpLightDuration = Duration.ofMinutes(30)
    }

    @Test
    fun testUpdateLedsNoAlarm() {
        alarmStore.setNextAlarm(null)

        sut.doJob(context)

        verify(device).channels
        verify(device, never()).render(any())

        Assert.assertEquals(0, sut.frame.toLong())
    }

    @Test
    fun testUpdateLedsFutureAlarmNearby() {
        alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)))

        sut.doJob(context)

        verify(device).channels
        verify(device).render(any())

        Assert.assertEquals(20, sut.frame.toLong())
    }

    @Test
    fun testUpdateLedsCleared() {
        alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)))
        sut.doJob(context)
        alarmStore.setNextAlarm(null)
        sut.doJob(context)

        verify(device, times(2)).channels
        verify(device, times(2)).render(argument.capture())

        val renderMap = argument.firstValue
        Assert.assertEquals(1, renderMap.size.toLong())
        Assert.assertEquals(LED_COUNT, renderMap[channel]?.count())// { c -> c.rgb == 0 })
        Assert.assertEquals(0, sut.frame.toLong())
    }

    companion object {
        private const val LED_COUNT = 300
    }
}
