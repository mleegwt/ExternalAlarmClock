package com.externalalarmclock.test

import java.awt.Color
import java.util.HashMap
import java.util.function.Function

import com.externalalarmclock.lib.rpiws281x.ERpiWs281xStripType
import com.externalalarmclock.lib.rpiws281x.RpiWs281x
import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.externalalarmclock.rpiws281x.RpiWs281xLibrary

class Main(private val clear: Boolean) {

    private val channel: RpiWs281xChannel
        get() {
            val channel = RpiWs281xChannel()
            channel.brightness = 255.toByte()
            channel.gpioNum = 18
            channel.ledCount = 300
            channel.stripType = ERpiWs281xStripType.SK6812_STRIP_GRBW
            return channel
        }

    fun showStableLeds(pixelSupplier: Function<RpiWs281xChannel, List<Color>>) {
        val dev = RpiWs281x(RpiWs281xLibrary.INSTANCE)

        val channel = channel

        dev.init(5, RpiWs281xLibrary.WS2811_TARGET_FREQ, channel)

        val pixels = getPixels(channel, if (clear) Function { this.getPixelList(it) } else pixelSupplier)

        dev.render(pixels)
        dev.fini()
    }

    private fun getPixels(channel: RpiWs281xChannel, pixelSupplier: Function<RpiWs281xChannel, List<Color>>): Map<RpiWs281xChannel, List<Color>> {
        val pixelsList = pixelSupplier.apply(channel)
        val pixels = HashMap<RpiWs281xChannel, List<Color>>()
        pixels[channel] = pixelsList
        return pixels
    }

    private fun getPixelList(channel: RpiWs281xChannel): List<Color> {
        return (0 until channel.ledCount).map { this.getColor(it) }
    }

    private fun getColor(pixel: Int): Color {
        when ((if (clear) 4 else pixel) % 5) {
            0 -> return Color(0x00FF0000, true)
            1 -> return Color(0x0000FF00, true)
            2 -> return Color(0x000000FF, true)
            3 -> return Color(-0x1000000, true)
            else -> return Color(0x00000000, true)
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main(true)
            main.showStableLeds(Function { main.getPixelList(it) })
        }
    }

}
