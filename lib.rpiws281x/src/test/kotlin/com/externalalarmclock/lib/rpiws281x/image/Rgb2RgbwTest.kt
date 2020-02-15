package com.externalalarmclock.lib.rpiws281x.image

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Rgb2RgbwTest {
    private val converter = Rgb2Rgbw()

    @Test
    fun testBlack() {
        Assertions.assertEquals(0, converter.getRgbw(0).toLong())
    }

    @Test
    fun testWhite() {
        Assertions.assertEquals(-0x1000000, converter.getRgbw(0x00FFFFFF).toLong())
    }

    @Test
    fun testRedish() {
        Assertions.assertEquals(0x19E60000, converter.getRgbw(0x00FF1919).toLong())
    }
}
