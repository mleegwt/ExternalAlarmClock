package com.externalalarmclock.lib.rpiws281x

import com.externalalarmclock.rpiws281x.RpiWs281xLibrary
import com.externalalarmclock.rpiws281x.ws2811_channel_t
import com.externalalarmclock.rpiws281x.ws2811_t
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import java.awt.*
import java.util.HashMap

@RunWith(MockitoJUnitRunner::class)
class RpiWs281xTest {
    @Mock
    private lateinit var lib: RpiWs281xLibrary

    @InjectMocks
    private lateinit var wrapper: RpiWs281x

    private val channel = RpiWs281xChannel(10, 18, 0xFF.toByte(), ERpiWs281xStripType.SK6812_STRIP_GRBW)

    @Test
    fun testGamma() {
        Assert.assertEquals(256, RpiWs281x.GAMMA.size.toLong())
    }

    @Test(expected = IllegalStateException::class)
    fun initFailedTest() {
        wrapper.init(1, 1, channel)
        wrapper.init(1, 1, channel)
    }

    @Test(expected = IllegalStateException::class)
    fun initFailedInitTest() {
        Mockito.`when`(lib.ws2811_init(Mockito.any())).thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_DMA)

        try {
            wrapper.init(1, 1, channel)
        } finally {
            Mockito.verify(lib).ws2811_init(Mockito.any())
        }
    }

    private fun fake(ws2811_channel_t: ws2811_channel_t, i: Int, j: Int) {}

    @Test
    fun happyFLowTest() {
        // Making sure to avoid the JNA part that breaks the test.
        wrapper.channel = this::fake

        wrapper.init(1, 1, channel)

        Assert.assertArrayEquals(arrayOf<Any>(channel), wrapper.channels.toTypedArray())

        Mockito.verify(lib).ws2811_init(Mockito.any())

        val pixels = HashMap<RpiWs281xChannel, List<Color>>()
        val colors = listOf(Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.WHITE,
                Color.GRAY, Color.PINK, Color.CYAN, Color.MAGENTA)
        pixels[channel] = colors
        wrapper.render(pixels)

        Mockito.verify(lib).ws2811_render(Mockito.any())
        Mockito.verify(lib).ws2811_wait(Mockito.any())

        wrapper.fini()

        Mockito.verify(lib).ws2811_fini(Mockito.any())

        // Verifying that we can restart now.
        wrapper.init(1, 1, channel)
    }

    @Test
    fun renderNotInitializedTest() {
        Assertions.assertThrows(IllegalStateException::class.java) { wrapper.render(HashMap()) }
    }

    @Test
    fun renderFailedTest() {
        Mockito.`when`(lib.ws2811_render(Mockito.any()))
                .thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_GENERIC)
        wrapper.init(1, 1, channel)
        Assertions.assertThrows(IllegalStateException::class.java) { wrapper.render(HashMap()) }
    }

    @Test
    fun renderWaitFailedTest() {
        Mockito.`when`(lib.ws2811_wait(Mockito.any())).thenReturn(RpiWs281xLibrary.ws2811_return_t.WS2811_ERROR_GENERIC)
        wrapper.init(1, 1, channel)
        Assertions.assertThrows(IllegalStateException::class.java) { wrapper.render(HashMap()) }
    }
}
