package com.externalalarmclock.lib.rpiws281x.image

import java.util.Collections.max
import java.util.Collections.min

/**
 * RGB to RGBW conversion.
 */
class Rgb2Rgbw {
    /**
     * Adapted from
     * https://stackoverflow.com/questions/40312216/converting-rgb-to-rgbw
     *
     * Using int, hex formatted as WWRRGGBB.
     *
     * @param rgb
     * @return rgbw
     */
    fun getRgbw(rgb: Int): Int {
        val Ri = rgb and 0x00FF0000 shr 16
        val Gi = rgb and 0x0000FF00 shr 8
        val Bi = rgb and 0x000000FF
        // Get the maximum between R, G, and B
        val tM = max(listOf(Ri, Gi, Bi))

        // If the maximum value is 0, immediately return pure black.
        if (tM == 0) {
            return 0x00000000
        }

        // This section serves to figure out what the color with 100% hue is
        val multiplier = (255.0f / tM).toDouble()
        val hR = Ri * multiplier
        val hG = Gi * multiplier
        val hB = Bi * multiplier

        // This calculates the Whiteness (not strictly speaking Luminance) of the color
        val max = max(listOf(hR, hG, hB))
        val min = min(listOf(hR, hG, hB))
        val Luminance = ((max + min) / 2.0f - 127.5f) * (255.0f / 127.5f) / multiplier

        // Calculate the output values
        // Trim them so that they are all between 0 and 255
        val Wo = fixRange(Luminance.toInt().toLong())
        val Bo = fixRange(Bi - Wo)
        val Ro = fixRange(Ri - Wo)
        val Go = fixRange(Gi - Wo)

        // Put it together in one integer
        return (Wo shl 24 or (Ro shl 16) or (Go shl 8) or Bo).toInt()
    }

    private fun fixRange(input: Long): Long {
        return when {
            input < 0 -> 0
            input > 255 -> 255
            else -> input
        }
    }
}
