package com.externalalarmclock.alarmclock.job

import org.mockito.Mockito
import org.slf4j.LoggerFactory

import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import org.junit.jupiter.api.Test
import org.quartz.JobExecutionContext

class StopJobTest {
    private val device = Mockito.mock(IRpiWs281x::class.java)
    private val sut = StopJob(LoggerFactory.getLogger(StopJob::class.java), device)
    private val context = Mockito.mock(JobExecutionContext::class.java)

    @Test
    fun testCloseDevice() {
        sut.doJob(context)

        Mockito.verify(device).fini()
    }
}
