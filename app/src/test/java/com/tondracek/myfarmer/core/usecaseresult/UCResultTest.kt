package com.tondracek.myfarmer.core.usecaseresult

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.util.setupTimberForTests
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UCResultTest {

    @Before
    fun setup() {
        setupTimberForTests()
    }

    @Test
    fun `Success stores data`() {
        val result = UCResult.Success(42)
        assertThat(result.data).isEqualTo(42)
    }

    @Test
    fun `mapSuccess transforms data`() {
        val result = UCResult.Success(10).mapSuccess { it * 2 }
        assertThat((result as UCResult.Success).data).isEqualTo(20)
    }

    @Test
    fun `mapSuccess on Failure returns same Failure`() {
        val failure = UCResult.Failure("User error")
        val mapped = failure.mapSuccess { 123 }

        assertThat(mapped).isEqualTo(failure)
    }

    @Test
    fun `mapSuccessFlat transforms data`() {
        val result = UCResult.Success(10)
            .mapSuccessFlat { UCResult.Success("Hello") }

        assertThat((result as UCResult.Success).data).isEqualTo("Hello")
    }

    @Test
    fun `mapSuccessFlat on Failure returns same Failure`() {
        val failure = UCResult.Failure("User error")
        val mapped = failure.mapSuccessFlat { UCResult.Success(123) }

        assertThat(mapped).isEqualTo(failure)
    }

    @Test
    fun `mapSuccessFlat on mapping Failure returns the mapping Failure`() {
        val mapFailure = UCResult.Failure("Another error")

        val mapped = UCResult.Success(10).mapSuccessFlat { mapFailure }

        assertThat(mapped).isEqualTo(mapFailure)
    }

    @Test
    fun `mapSuccessFlat on double Failure returns the first Failure`() {
        val failure = UCResult.Failure("User error")
        val mapFailure = UCResult.Failure("Another error")

        val mapped = failure.mapSuccessFlat { mapFailure }

        assertThat(mapped).isEqualTo(failure)
    }

    @Test
    fun `getOrNull on Success returns data`() {
        assertThat(UCResult.Success("A").getOrNull()).isEqualTo("A")
    }

    @Test
    fun `getOrThrow on Success returns data`() {
        assertThat(UCResult.Success("A").getOrThrow()).isEqualTo("A")
    }

    @Test
    fun `fold on Success invokes onSuccess`() {
        val result = UCResult.Success(5).fold(
            onSuccess = { it * 2 },
            onFailure = { -1 }
        )
        assertThat(result).isEqualTo(10)
    }

    @Test
    fun `withSuccess executes block and returns original result`() = runTest {
        var executed = false
        val result = UCResult.Success("X")
            .withSuccess { executed = true }

        assertThat(executed).isTrue()
        assertThat(result).isInstanceOf(UCResult.Success::class.java)
    }

    // --- Failure tests ---

    @Test
    fun `Failure stores user and system error`() {
        val failure = UCResult.Failure("User error", "System error")
        assertThat(failure.userError).isEqualTo("User error")
        assertThat(failure.systemError).isEqualTo("System error")
    }

    @Test
    fun `Failure secondary constructor uses throwable message`() {
        val throwable = IllegalStateException("Boom")
        val failure = UCResult.Failure("Oops", throwable)

        assertThat(failure.userError).isEqualTo("Oops")
        assertThat(failure.systemError).isEqualTo("Boom")
    }

    @Test
    fun `getOrNull on Failure returns null`() {
        assertNull(UCResult.Failure("X").getOrNull())
    }

    @Test(expected = Throwable::class)
    fun `getOrThrow on Failure throws`() {
        UCResult.Failure("X", "Failure").getOrThrow()
    }

    @Test
    fun `fold on Failure invokes onFailure`() {
        val result = UCResult.Failure("Err").fold(
            onSuccess = { 1 },
            onFailure = { -1 }
        )
        assertThat(result).isEqualTo(-1)
    }

    @Test
    fun `withFailure executes block and returns original result`() = runTest {
        var executed = false
        val result = UCResult.Failure("X")
            .withFailure { executed = true }

        assertThat(executed).isTrue()
        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `withFailure does not execute block on Success`() = runTest {
        var executed = false
        val result = UCResult.Success("X")
            .withFailure { executed = true }

        assertThat(executed).isFalse()
        assertThat(result).isInstanceOf(UCResult.Success::class.java)
    }
}
