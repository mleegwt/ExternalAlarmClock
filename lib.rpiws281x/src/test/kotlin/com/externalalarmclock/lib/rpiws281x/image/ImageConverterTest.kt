package com.externalalarmclock.lib.rpiws281x.image

import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.util.Arrays

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.madgag.gif.fmsware.GifDecoder

@RunWith(MockitoJUnitRunner::class)
class ImageConverterTest {

    private val decoder = Mockito.mock(GifDecoder::class.java)

    private val frame0 = BufferedImage(10, 6, BufferedImage.TYPE_INT_ARGB)
    private val frame1 = BufferedImage(10, 6, BufferedImage.TYPE_INT_ARGB)
    private val destination = Rectangle(5, 3)
    private val converter = ImageConverter(destination, true, EStartCorner.BOTTOM_RIGHT, decoder)

    private val channel = RpiWs281xChannel(ledCount = 15)

    @Before
    fun setUp() {
        createFrame0()
        createFrame1()
    }

    @Test
    fun test() {
        Mockito.`when`(decoder.frameCount).thenReturn(2)
        Mockito.`when`(decoder.getFrame(Mockito.eq(0))).thenReturn(frame0)
        Mockito.`when`(decoder.getFrame(Mockito.eq(1))).thenReturn(frame1)

        Assert.assertEquals(2, converter.frames.toLong())

        assertColorsEqual(FRAME0_COLORS, converter.getBorderPixels(0, channel))
        assertColorsEqual(FRAME1_COLORS, converter.getBorderPixels(1, channel))
    }

    private fun assertColorsEqual(expected: List<Color>, actual: List<Color>) {
        Assert.assertEquals(expected.map(this::format), actual.map(this::format))
    }

    private fun format(color: Color): String {
        return String.format("%06X", color.rgb and 0xFFFFFF)
    }

    private fun createFrame0() {
        setColor(frame0, 2, 7, 0, 1, Color.GREEN.rgb)
        setColor(frame0, 2, 7, 2, 3, Color.GRAY.rgb)
        setColor(frame0, 2, 7, 4, 5, Color.WHITE.rgb)
        setColor(frame0, 0, 1, 2, 3, Color.BLUE.rgb)
        setColor(frame0, 8, 9, 2, 3, Color.RED.rgb)
    }

    private fun createFrame1() {
        setColor(frame1, 3, 6, 0, 1, Color.GREEN.rgb)
        setColor(frame1, 2, 7, 2, 3, Color.GRAY.rgb)
        setColor(frame1, 3, 6, 4, 5, Color.WHITE.rgb)
        setColor(frame1, 0, 1, 2, 3, Color.BLUE.rgb)
        setColor(frame1, 8, 9, 2, 3, Color.RED.rgb)
    }

    private fun setColor(image: BufferedImage, startX: Int, endX: Int, startY: Int, endY: Int, rgb: Int) {
        for (x in startX..endX) {
            for (y in startY..endY) {
                image.setRGB(x, y, rgb)
            }
        }
    }

    companion object {
        private val FRAME0_COLORS = Arrays.asList(Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE,
                Color.BLACK, Color.BLUE, Color.BLACK, Color.GREEN, Color.GREEN, Color.GREEN, Color.BLACK, Color.RED,
                Color.BLACK, Color.BLACK, Color.BLACK)
        private val FRAME1_COLORS = Arrays.asList(Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK,
                Color.BLACK, Color.BLUE, Color.BLACK, Color.BLACK, Color.GREEN, Color.GREEN, Color.BLACK, Color.RED,
                Color.BLACK, Color.BLACK, Color.BLACK)
    }
}

/**
 * java.lang.AssertionError: expected:<[
 *
 * {000000}, {FFFFFF}, {FFFFFF}, {FFFFFF}, {000000}, {0000FF}, {000000},
 * {00FF00}, {00FF00}, {00FF00}, {000000}, {FF0000}
 *
 * ]> but was:<[
 *
 * {000000}, {FFFFFF}, {FFFFFF}, {000000}, {000000}, {0000FF}, {000000},
 * {000000}, {00FF00}, {00FF00}, {000000}, {FF0000}
 *
 * ]>
 */
