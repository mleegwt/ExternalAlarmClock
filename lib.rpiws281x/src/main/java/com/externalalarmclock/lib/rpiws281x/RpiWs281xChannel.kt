package com.externalalarmclock.lib.rpiws281x

data class RpiWs281xChannel(
    val ledCount: Int = 0,
    val gpioNum: Int = 0,
    var brightness: Byte = 0,
    val stripType: ERpiWs281xStripType = ERpiWs281xStripType.SK6812_STRIP_GRBW
)
