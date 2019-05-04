package com.externalalarmclock.alarmclock.job

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.quartz.JobExecutionException
import org.slf4j.LoggerFactory

import com.externalalarmclock.alarmclock.job.StopJob
import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import org.quartz.JobExecutionContext

class StopJobTest {
    private val sut = StopJob(LoggerFactory.getLogger(StopJob::class.java))
    private val device = Mockito.mock(IRpiWs281x::class.java)
    private val context = Mockito.mock(JobExecutionContext::class.java)

    @Before
    fun setUp() {
        sut.setDevice(device)
    }

    @Test
    fun testCloseDevice() {
        sut.doJob(context)

        Mockito.verify(device).fini()
    }
}
