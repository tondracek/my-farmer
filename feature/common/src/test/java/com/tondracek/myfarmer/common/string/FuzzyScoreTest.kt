package com.tondracek.myfarmer.common.string

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FuzzyScoreTest {

    @Test
    fun `should return 0 for empty or blank query`() {
        assertThat(fuzzyScore("", "target")).isEqualTo(0)
        assertThat(fuzzyScore(" ", "target")).isEqualTo(0)
    }

    @Test
    fun `should return 0 for empty or blank target`() {
        assertThat(fuzzyScore("query", "")).isEqualTo(0)
        assertThat(fuzzyScore("query", " ")).isEqualTo(0)
    }

    @Test
    fun `should return correct score for perfect match`() {
        // Query "abc" on "abc".
        // Score: 3 matches * 10 + 5 (start bonus) = 35
        assertThat(fuzzyScore("abc", "abc")).isEqualTo(35)
    }

    @Test
    fun `should handle case and diacritics using normalization`() {
        // Query "Česko" on "čEsKo".
        // Normalized: "cesko" on "cesko".
        // Score: 5 matches * 10 + 5 (start bonus) = 55
        assertThat(fuzzyScore("Česko", "čEsKo")).isEqualTo(55)
    }

    @Test
    fun `should apply penalties for skipped characters`() {
        // Query "a c" on "a b c". Normalized is "a c" on "a b c".
        // 'a': +10. ti=1
        // ' ': +10. ti=2 (t[1] is ' ')
        // 'c': skip 'b' (-1), skip ' ' (-1), match 'c' (+10).
        // Total match score: 10 + 10 + (-1 - 1 + 10) = 28
        // Start bonus: 'a b c'.startsWith('a') = +5
        // Final score: 33
        assertThat(fuzzyScore("a c", "a b c")).isEqualTo(33)
    }

    @Test
    fun `should return 0 if a query character is not found in the target`() {
        // Query "ax" on "a".
        // 'a' found: +10. ti=1.
        // 'x' not found. Returns 0.
        assertThat(fuzzyScore("ax", "a")).isEqualTo(0)
    }

    @Test
    fun `should return 0 if a query character is out of order`() {
        // Query "ba" on "ab".
        // 'b' found: skip 'a' (-1), match 'b' (+10). score=9. ti=2.
        // 'a' is searched from ti=2 (end of target). Not found. Returns 0.
        assertThat(fuzzyScore("ba", "ab")).isEqualTo(0)
    }

    @Test
    fun `should apply start bonus correctly when first char matches`() {
        // Query "a" on "az".
        // 'a': +10. ti=1.
        // Start bonus: "az" starts with 'a'. +5.
        // Total: 15.
        assertThat(fuzzyScore("a", "az")).isEqualTo(15)
    }

    @Test
    fun `should not apply start bonus when first char does not match`() {
        // Query "a" on "za".
        // 'a': skip 'z' (-1). match 'a' (+10). score=9. ti=2.
        // Start bonus: "za" does not start with 'a'. +0.
        // Total: 9.
        assertThat(fuzzyScore("a", "za")).isEqualTo(9)
    }

    @Test
    fun `should handle a complex real-world example with normalization and gaps`() {
        // Query "vc" on "Domácí Včelařství"
        // Normalized: q="vc", t="domaci vcelarstvi"
        // 'v': skip 'd'...'i', ' ' (-7). match 'v' (+10). score=3. ti=8.
        // 'c': match 'c' (+10). score=13. ti=9.
        // Start bonus: t="domaci vcelarstvi".startsWith('v'). No. +0.
        // Final score: 13.
        assertThat(fuzzyScore("vc", "Domácí Včelařství")).isEqualTo(13)
    }
}
