package com.externalalarmclock.alarmclock.job

import com.externalalarmclock.alarmclock.AlarmStore
import com.externalalarmclock.alarmclock.resources.SetNextAlarmResource
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger

import com.externalalarmclock.lib.rpiws281x.IRpiWs281x
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.externalalarmclock.lib.rpiws281x.image.EStartCorner
import com.externalalarmclock.lib.rpiws281x.image.ImageConverter
import com.madgag.gif.fmsware.GifDecoder

import io.dropwizard.jobs.Job
import io.dropwizard.jobs.annotations.DelayStart
import io.dropwizard.jobs.annotations.Every
import io.dropwizard.jobs.annotations.OnApplicationStop
import java.awt.Color
import java.awt.Rectangle
import java.time.Duration
import java.time.ZonedDateTime
import java.util.HashMap

@OnApplicationStop
class StopJob(private val logger: Logger, private val device: IRpiWs281x) : Job() {
    @Throws(JobExecutionException::class)
    override fun doJob(context: JobExecutionContext) {
        logger.debug("Stopping application")
        device.fini()
        logger.info("Alarmclock has stopped")
    }
}

@DelayStart("5s")
@Every("1s")
class UpdateLedsJob(private val logger: Logger, private val alarmStore: AlarmStore, private val device: IRpiWs281x) :
    Job() {
    private val converter: ImageConverter = createConverter()
    private var frameCount = -1
    var frame: Int = 0
        private set

    private val noColor = Color(0x00000000, true)

    override fun doJob(context: JobExecutionContext) {
        val channels = device.channels

        val pixels = HashMap<RpiWs281xChannel, List<Color>>()
        for (channel in channels) {
            createPixelsForChannel(pixels, channel)
        }

        if (pixels.isEmpty()) {
            return
        }

        device.render(pixels)
    }

    private fun createPixelsForChannel(pixels: MutableMap<RpiWs281xChannel, List<Color>>, channel: RpiWs281xChannel) {
        val now = ZonedDateTime.now()
        val alarm = alarmStore.getNextAlarm(channel)
        val end = alarm?.first
        val start = alarm?.second

        if (start == null || now.isBefore(start) || now.isAfter(end?.plusSeconds(60))) {
            if (frameCount != -1) {
                pixels[channel] = getOffPixelList(channel)
            }
            logger.debug("Before alarm start {}-{}", start, end)
            frameCount = -1
            frame = 0
        } else if (now.isAfter(start) && now.isBefore(end?.plusSeconds(60))) {
            val afterStart = Duration.between(now, start)
            val totalDuration = Duration.between(end, start)
            frame = (converter.frames * afterStart.toNanos() / totalDuration.toNanos()).toInt()
            if (frame > converter.frames) {
                frame = converter.frames - 1
            }

            if (frame != frameCount) {
                pixels[channel] = converter.getBorderPixels(frame, channel)
                logger.info("During wakeup light {}-{}, frame {}", start, end, frame)
                frameCount = frame
            }
        }
    }

    private fun createConverter(): ImageConverter {
        val r = Rectangle(0, 0, 32, 18)
        val resourceStream = SetNextAlarmResource::class.java.getResourceAsStream("/sunup.gif")
        val decoder = GifDecoder()
        decoder.read(resourceStream)
        return ImageConverter(r, false, EStartCorner.BOTTOM_RIGHT, decoder)
    }

    private fun getOffPixelList(channel: RpiWs281xChannel): List<Color> {
        return (0 until channel.ledCount).map { noColor }
    }
}
