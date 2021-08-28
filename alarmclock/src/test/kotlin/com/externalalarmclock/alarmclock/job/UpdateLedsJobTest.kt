package com.externalalarmclock.alarmclock.job

import java.awt.Color
import java.time.Duration
import java.time.ZonedDateTime

import org.slf4j.LoggerFactory

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.quartz.JobExecutionContext

@ExtendWith(MockitoExtension::class)
class UpdateLedsJobTest {
    private val alarmStore = AlarmStore()
    private val channel = RpiWs281xChannel(ledCount = LED_COUNT)
    private val device = mock<IRpiWs281x> {
        on { channels } doReturn listOf(channel)
    }
    private val sut = UpdateLedsJob(LoggerFactory.getLogger(UpdateLedsJob::class.java), alarmStore, device)
    private val context = mock<JobExecutionContext> {}
    private val argument = argumentCaptor<(Map<RpiWs281xChannel, List<Color>>)>()

    @BeforeEach
    fun setUp() {
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

        Assertions.assertEquals(0, sut.frame.toLong())
    }

    @Test
    fun testUpdateLedsFutureAlarmNearby() {
        alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)))

        sut.doJob(context)

        verify(device).channels
        verify(device).render(any())

        Assertions.assertEquals(20, sut.frame.toLong())
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
        Assertions.assertEquals(1, renderMap.size.toLong())
        Assertions.assertEquals(LED_COUNT, renderMap[channel]?.count())// { c -> c.rgb == 0 })
        Assertions.assertEquals(0, sut.frame.toLong())
    }

    companion object {
        private const val LED_COUNT = 300
    }
}
