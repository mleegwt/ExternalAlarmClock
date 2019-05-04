package com.externalalarmclock.alarmclock.job

import java.awt.Color
import java.time.Duration
import java.time.ZonedDateTime
import java.util.Arrays

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.quartz.JobExecutionException
import org.slf4j.LoggerFactory

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.alarmclock.job.UpdateLedsJob
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import org.junit.runner.RunWith
import org.mockito.Captor
import org.mockito.junit.MockitoJUnitRunner
import org.quartz.JobExecutionContext

@RunWith(MockitoJUnitRunner::class)
class UpdateLedsJobTest {
    private val alarmStore = AlarmStore()
    private val sut = UpdateLedsJob(LoggerFactory.getLogger(UpdateLedsJob::class.java), alarmStore)
    private val device = Mockito.mock(IRpiWs281x::class.java)
    private val channel = RpiWs281xChannel()
    private val context = Mockito.mock(JobExecutionContext::class.java)

    @Captor
    private lateinit var argument : ArgumentCaptor<Map<RpiWs281xChannel, List<Color>>>

    @Before
    fun setUp() {
        sut.setDevice(device)

        channel.ledCount = LED_COUNT

        val channels = Arrays.asList(channel)
        alarmStore.addChannels(channels)
        Mockito.`when`(device.channels).thenReturn(channels)
    }

    @Test
    @Throws(JobExecutionException::class)
    fun testUpdateLedsNoAlarm() {
        alarmStore.setNextAlarm(null)

        sut.doJob(context)

        Mockito.verify(device).channels
        Mockito.verify(device, Mockito.never()).render(Mockito.any<Map<RpiWs281xChannel, List<Color>>>())

        Assert.assertEquals(0, sut.frame.toLong())
    }

    @Test
    @Throws(JobExecutionException::class)
    fun testUpdateLedsFutureAlarmNearby() {
        alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)))

        sut.doJob(context)

        Mockito.verify(device).channels
        Mockito.verify(device).render(Mockito.any<Map<RpiWs281xChannel, List<Color>>>())

        Assert.assertEquals(20, sut.frame.toLong())
    }

    @Test
    @Throws(JobExecutionException::class)
    fun testUpdateLedsCleared() {
        alarmStore.setNextAlarm(ZonedDateTime.now().plus(Duration.ofMinutes(20)))
        sut.doJob(context)
        alarmStore.setNextAlarm(null)
        sut.doJob(context)

        Mockito.verify(device, Mockito.times(2)).channels
        Mockito.verify(device, Mockito.times(2)).render(argument.capture())

        val renderMap = argument.value
        Assert.assertEquals(1, renderMap.size.toLong())
        Assert.assertEquals(LED_COUNT, renderMap[channel]?.count { c -> c.rgb == 0 })
        Assert.assertEquals(0, sut.frame.toLong())
    }

    companion object {
        private const val LED_COUNT = 300
    }
}



