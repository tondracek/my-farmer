package com.tondracek.myfarmer.common.string

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NormalizeTest {

    @Test
    fun `should convert to lowercase`() {
        assertThat(normalize("HeLlO")).isEqualTo("hello")
    }

    @Test
    fun `should handle empty string`() {
        assertThat(normalize("")).isEqualTo("")
    }

    @Test
    fun `should not change already normalized string`() {
        assertThat(normalize("normal string")).isEqualTo("normal string")
    }

    @Test
    fun `should correctly replace czech slovak diacritics`() {
        val original =
            "aábcčdďeěéfghiíjklmnoóôpqrřsštťuúůvwxyýzžAÁBCČDĎEĚÉFGHHIÍJKLMNOÓÔPQRŘSŠTŤUÚŮVWXYÝZŽ0123456789!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~ "
        val expected =
            "aabccddeeefghiijklmnooopqrrssttuuuvwxyyzzaabccddeeefghhiijklmnooopqrrssttuuuvwxyyzz0123456789!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~ "

        assertThat(normalize(original)).isEqualTo(expected)
    }

    @Test
    fun `should handle mixed text with diacritics and capitalization`() {
        val original = "Příliš žluťoučký kůň Úpěl Ďábelské ódy"
        val expected = "prilis zlutoucky kun upel dabelske ody"

        assertThat(normalize(original)).isEqualTo(expected)
    }
}
