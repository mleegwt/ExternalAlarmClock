package com.externalalarmclock.lib.rpiws281x

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary

enum class ERpiWs281xStripType private constructor(val libStripType: Int) {
    SK6812_STRIP_GRBW(RpiWs281xLibrary.SK6812_STRIP_GRBW)
}
