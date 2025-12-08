package com.tondracek.myfarmer.common.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FuzzyScoreTest {

    @Test
    fun `exact match gives full score`() {
        val score = fuzzyScore("apple", "apple")
        assertThat(score).isGreaterThan(40) // 5 chars * 10 points + start bonus
    }

    @Test
    fun `characters must appear in order`() {
        val score1 = fuzzyScore("abc", "a_b_c")
        val score2 = fuzzyScore("abc", "cba")

        assertThat(score1).isGreaterThan(0)
        assertThat(score2).isEqualTo(0)
    }

    @Test
    fun `partial match has penalties`() {
        val score1 = fuzzyScore("abc", "axxbc")
        val score2 = fuzzyScore("abc", "abc")

        assertThat(score1).isLessThan(score2)
    }

    @Test
    fun `missing character returns zero`() {
        assertThat(fuzzyScore("abc", "ab")).isEqualTo(0)
    }

    @Test
    fun `blank query or target returns zero`() {
        assertThat(fuzzyScore("", "apple")).isEqualTo(0)
        assertThat(fuzzyScore("abc", "")).isEqualTo(0)
    }

    @Test
    fun `start bonus is applied`() {
        val score1 = fuzzyScore("a", "apple") // starts with 'a'
        val score2 = fuzzyScore("a", "banana")

        assertThat(score1).isGreaterThan(score2)
    }

    @Test
    fun `normalizes diacritics`() {
        val score1 = fuzzyScore("rizek", "řízek")
        val score2 = fuzzyScore("rizek", "rizek")

        assertThat(score1).isEqualTo(score2)
    }

    @Test
    fun `case does not matter`() {
        val score1 = fuzzyScore("Apple", "APPLE")
        val score2 = fuzzyScore("apple", "apple")

        assertThat(score1).isEqualTo(score2)
    }

    @Test
    fun `query longer than target always returns zero`() {
        assertThat(fuzzyScore("abcdef", "abc")).isEqualTo(0)
    }

    @Test
    fun `penalties accumulate when skipping characters`() {
        val score1 = fuzzyScore("ac", "a---c")
        val score2 = fuzzyScore("ac", "ac")

        assertThat(score1).isLessThan(score2)
    }

    @Test
    fun `match fails as soon as a character cannot be found`() {
        assertThat(fuzzyScore("car", "cazzz")).isEqualTo(0)
    }
}
