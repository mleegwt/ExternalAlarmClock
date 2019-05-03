package com.externalalarmclock.lib.rpiws281x.image

import java.awt.Color
import java.awt.Rectangle
import java.awt.Shape
import java.awt.image.BufferedImage
import java.util.ArrayList

import com.externalalarmclock.lib.rpiws281x.RpiWs281xChannel
import com.madgag.gif.fmsware.GifDecoder

class ImageConverter(destination: Rectangle, private val includeCorners: Boolean, private val startCorner: EStartCorner,
                     private val decoder: GifDecoder) {
    private val destination: Shape
    private var width: Int = 0
    private var height: Int = 0

    val frames: Int
        get() = decoder.frameCount

    init {
        this.destination = destination
    }

    fun getBorderPixels(frame: Int, channel: RpiWs281xChannel): List<Color> {
        val image = decoder.getFrame(frame)

        val bounds = destination.bounds2D
        width = bounds.width.toInt()
        height = bounds.height.toInt()

        val scaled = getScaledImage(image)

        val top = ArrayList<Color>()
        val bottom = ArrayList<Color>()
        val left = ArrayList<Color>()
        val right = ArrayList<Color>()
        getImageBorders(scaled, top, bottom, left, right)

        // Assuming clockwise
        left.reverse()
        bottom.reverse()

        val result = createLedStripContents(top, bottom, left, right)

        return result + (result.size until channel.ledCount).map {
            Color(0x00000000, true)
        }
    }

    private fun getImageBorders(scaled: BufferedImage, top: MutableList<Color>, bottom: MutableList<Color>, left: MutableList<Color>,
                                right: MutableList<Color>) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = getPixelColor(scaled, x, y) ?: continue

                addColorToBorder(top, bottom, left, right, x, y, color)
            }
        }
    }

    private fun addColorToBorder(top: MutableList<Color>, bottom: MutableList<Color>, left: MutableList<Color>, right: MutableList<Color>, x: Int,
                                 y: Int, color: Color) {
        when {
            y == 0 -> top.add(color)
            y == height - 1 -> bottom.add(color)
            x == 0 -> left.add(color)
            x == width - 1 -> right.add(color)
        }
    }

    private fun getPixelColor(scaled: BufferedImage, x: Int, y: Int): Color? {
        var color: Color? = null
        if ((x == 0 || x == width - 1) && (y == 0 || y == height - 1)) {
            if (includeCorners) {
                color = Color(scaled.getRGB(x, y) and 0x00FFFFFF, true)
            }
        } else {
            color = Color(scaled.getRGB(x, y) and 0x00FFFFFF, true)
        }
        return color
    }

    private fun createLedStripContents(top: List<Color>, bottom: List<Color>, left: List<Color>,
                                       right: List<Color>): List<Color> {
        return when (startCorner) {
            EStartCorner.BOTTOM_LEFT -> left + top + right + bottom
            EStartCorner.BOTTOM_RIGHT -> bottom + left + top + right
            EStartCorner.TOP_LEFT -> top + right + bottom + left
            EStartCorner.TOP_RIGHT -> right + bottom + left + top
            else -> throw IllegalArgumentException("Unexpected start corder $startCorner")
        }
    }

    // Poor man solution, no need for X environment.
    private fun getScaledImage(src: BufferedImage): BufferedImage {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val ww = src.width
        val hh = src.height
        for (x in 0 until width) {
            for (y in 0 until height) {
                val col = src.getRGB(x * ww / width, y * hh / height)
                img.setRGB(x, y, col and 0x00FFFFFF)
            }
        }
        return img
    }
}
