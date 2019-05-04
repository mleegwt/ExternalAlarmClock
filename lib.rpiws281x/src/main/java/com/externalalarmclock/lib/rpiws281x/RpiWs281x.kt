package com.externalalarmclock.lib.rpiws281x

import java.awt.Color
import java.util.HashMap

import com.externalalarmclock.lib.rpiws281x.image.Rgb2Rgbw
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary
import com.externalalarmclock.rpiws281x.ws2811_channel_t
import com.externalalarmclock.rpiws281x.ws2811_t
import com.sun.jna.Native

class RpiWs281x(private val lib: RpiWs281xLibrary) : IRpiWs281x {
    private val strip = ws2811_t()
    private val channelIndexMap = HashMap<RpiWs281xChannel, Int>()
    private var channelIndex: Int = 0

    private var initialized: Boolean = false

    var channel = this::setLed

    override val channels: Collection<RpiWs281xChannel>
        get() = channelIndexMap.keys

    override fun init(dma: Int, freq: Int, vararg channels: RpiWs281xChannel) {
        if (initialized) {
            throw IllegalStateException("Already initialized")
        }
        channelIndex = 0
        strip.dmanum = dma
        strip.freq = freq

        val libChannels = listOf(*channels).map { this.getLibChannel(it) }
        // Adding empty channel to make sure the end of the series is detected
        strip.channel = (libChannels + ws2811_channel_t()).toTypedArray()

        val ret = lib.ws2811_init(strip)
        if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
            throw IllegalStateException("Failed to start strip due to " + lib.ws2811_get_return_t_str(ret))
        }

        initialized = true
    }

    private fun getLibChannel(channel: RpiWs281xChannel): ws2811_channel_t {
        val libChannel = ws2811_channel_t()
        libChannel.strip_type = channel.stripType.libStripType
        libChannel.brightness = channel.brightness
        libChannel.count = channel.ledCount
        libChannel.gpionum = channel.gpioNum

        channelIndexMap[channel] = channelIndex++
        return libChannel
    }

    override fun render(pixels: Map<RpiWs281xChannel, List<Color>>) {
        if (!initialized) {
            throw IllegalStateException("Not initialized yet")
        }
        pixels.entries.forEach { e -> this.prepare(e.key, e.value) }

        var ret = lib.ws2811_render(strip)
        if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
            throw IllegalStateException("Failed to render strip due to " + lib.ws2811_get_return_t_str(ret))
        }
        ret = lib.ws2811_wait(strip)
        if (ret != RpiWs281xLibrary.ws2811_return_t.WS2811_SUCCESS) {
            throw IllegalStateException("Failed to end rendering strip due to " + lib.ws2811_get_return_t_str(ret))
        }
    }

    private fun prepare(channel: RpiWs281xChannel, pixels: List<Color>) {
        val ledCount = channel.ledCount
        val libChannel = strip.channel[channelIndexMap[channel]!!]
        val convert = Rgb2Rgbw()
        for (i in 0 until ledCount) {
            val correctedPixel = convert.getRgbw(corectGamma(pixels[i].rgb))
            this.channel(libChannel, i, correctedPixel)
        }
    }

    private fun setLed(libChannel: ws2811_channel_t, i: Int, correctedPixel: Int) {
        val intSize = Native.getNativeSize(Int::class.javaPrimitiveType).toLong()
        val byteBuffer = libChannel.leds.pointer.getByteBuffer(i * intSize, intSize)
        byteBuffer.asIntBuffer().put(correctedPixel)
    }

    private fun corectGamma(rgb: Int): Int {
        val correctedW = GAMMA[rgb and (0x0FF000000L shr 24).toInt()].toByte()
        val correctedR = GAMMA[rgb and 0x00FF0000 shr 16].toByte()
        val correctedG = GAMMA[rgb and 0x0000FF00 shr 8].toByte()
        val correctedB = GAMMA[rgb and 0x000000FF].toByte()

        return (correctedW.toInt() shl 24 and -0x1000000 or (correctedR.toInt() shl 16 and 0x00FF0000) or (correctedG.toInt() shl 8 and 0x0000FF00)
                or (correctedB.toInt() and 0x000000FF))
    }

    override fun fini() {
        lib.ws2811_fini(strip)

        initialized = false
    }

    companion object {
        val GAMMA = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 21, 21, 22, 22, 23, 23, 24, 25, 25, 26, 27, 27, 28, 29, 29, 30, 31, 31, 32, 33, 34, 34, 35, 36, 37, 37, 38, 39, 40, 40, 41, 42, 43, 44, 45, 46, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 76, 77, 78, 79, 80, 81, 83, 84, 85, 86, 88, 89, 90, 91, 93, 94, 95, 96, 98, 99, 100, 102, 103, 104, 106, 107, 109, 110, 111, 113, 114, 116, 117, 119, 120, 121, 123, 124, 126, 128, 129, 131, 132, 134, 135, 137, 138, 140, 142, 143, 145, 146, 148, 150, 151, 153, 155, 157, 158, 160, 162, 163, 165, 167, 169, 170, 172, 174, 176, 178, 179, 181, 183, 185, 187, 189, 191, 193, 194, 196, 198, 200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 222, 224, 227, 229, 231, 233, 235, 237, 239, 241, 244, 246, 248, 250, 252, 255)
    }
}
