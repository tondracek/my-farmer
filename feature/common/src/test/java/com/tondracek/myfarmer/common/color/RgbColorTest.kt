package com.tondracek.myfarmer.common.color

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RgbColorTest {

    @Test
    fun `toArgb and fromArgb should be idempotent`() {
        val original = RgbColor(100, 150, 200)

        val argb = original.toArgb()
        val mappedBack = RgbColor.fromArgb(argb)

        assertThat(mappedBack).isEqualTo(original)
    }
}
