package com.externalalarmclock.lib.rpiws281x

import java.awt.Color

interface IRpiWs281x {
    val channels: Collection<RpiWs281xChannel>
    fun init(dma: Int, freq: Int, vararg channels: RpiWs281xChannel)

    fun render(pixels: Map<RpiWs281xChannel, List<Color>>)

    fun fini()
}