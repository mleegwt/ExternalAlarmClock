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

import de.spinscale.dropwizard.jobs.Job
import de.spinscale.dropwizard.jobs.annotations.DelayStart
import de.spinscale.dropwizard.jobs.annotations.Every
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStop
import java.awt.Color
import java.awt.Rectangle
import java.time.Duration
import java.time.ZonedDateTime
import java.util.HashMap
import java.util.stream.Collectors
import java.util.stream.IntStream

@OnApplicationStop
class StopJob(private val logger: Logger) : Job() {
    private var device: IRpiWs281x? = null

    @Throws(JobExecutionException::class)
    override fun doJob(context: JobExecutionContext) {
        logger.debug("Stopping application")
        device?.fini()
        logger.info("Alarmclock has stopped")
    }

    fun setDevice(device: IRpiWs281x) {
        this.device = device
    }
}

@DelayStart("5s")
@Every("1s")
class UpdateLedsJob(private val logger: Logger, private val alarmStore: AlarmStore) : Job() {
    private lateinit var device: IRpiWs281x
    private var converter: ImageConverter? = null
    private var frameCount = -1
    var frame: Int = 0
        private set

    private val noColor = Color(0x00000000, true)

    @Throws(JobExecutionException::class)
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
        val end = alarmStore.getNextAlarm(channel)
        val start = end?.minusMinutes(30)

        if (start == null || now.isBefore(start) || now.isAfter(end)) {
            if (frameCount != -1) {
                pixels[channel] = getOffPixelList(channel)
            }
            logger.debug("Before alarm start {}-{}", start, end)
            converter = null
            frameCount = -1
            frame = 0
        } else if (now.isAfter(start) && now.isBefore(end)) {
            if (converter == null) {
                val r = Rectangle(0, 0, 32, 18)
                val resourceStream = SetNextAlarmResource::class.java.getResourceAsStream("/sunup.gif")
                val decoder = GifDecoder()
                decoder.read(resourceStream)
                converter = ImageConverter(r, false, EStartCorner.BOTTOM_RIGHT, decoder)
            }

            val afterStart = Duration.between(now, start)
            val totalDuration = Duration.between(end, start)
            frame = (converter!!.frames * afterStart.toNanos() / totalDuration.toNanos()).toInt()

            if (frame != frameCount) {
                pixels[channel] = converter!!.getBorderPixels(frame, channel)
                logger.info("During wakeup light {}-{}, frame {}", start, end, frame)
                frameCount = frame
            }
        }
    }

    fun setDevice(device: IRpiWs281x) {
        this.device = device
    }

    private fun getOffPixelList(channel: RpiWs281xChannel): List<Color> {
        return (0 until channel.ledCount).map { this.noColor }
    }
}
