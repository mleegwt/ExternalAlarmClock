package com.externalalarmclock.lib.rpiws281x.image

import org.junit.Assert
import org.junit.Test

class Rgb2RgbwTest {
    private val converter = Rgb2Rgbw()

    @Test
    fun testBlack() {
        Assert.assertEquals(0, converter.getRgbw(0).toLong())
    }

    @Test
    fun testWhite() {
        Assert.assertEquals(-0x1000000, converter.getRgbw(0x00FFFFFF).toLong())
    }

    @Test
    fun testRedish() {
        Assert.assertEquals(0x19E60000, converter.getRgbw(0x00FF1919).toLong())
    }
}
