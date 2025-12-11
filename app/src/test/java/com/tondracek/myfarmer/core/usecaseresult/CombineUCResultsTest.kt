package com.tondracek.myfarmer.core.usecaseresult

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.util.setupTimberForTests
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CombineUCResultsTest {

    @Before
    fun setUp() {
        setupTimberForTests()
    }

    @Test
    fun `2-flow combine returns transform result when both are Success`() = runTest {
        val f0 = flowOf(UCResult.Success(2))
        val f1 = flowOf(UCResult.Success(3))

        val result = combineUCResults(
            f0, f1,
            onFirstFailure = { -1 }
        ) { a, b -> a + b }
            .first()

        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `2-flow combine returns onFirstFailure when one fails`() = runTest {
        val f0: Flow<UCResult<Int>> = flowOf(UCResult.Success(10))
        val f1: Flow<UCResult<Int>> = flowOf(UCResult.Failure("Boom"))

        val result = combineUCResults(
            f0,
            f1,
            onFirstFailure = { -999 }
        ) { a, b -> a + b }
            .first()

        assertThat(result).isEqualTo(-999)
    }

    @Test
    fun `first failure is the one forwarded to onFirstFailure`() = runTest {
        val failure1 = UCResult.Failure("First")
        val failure2 = UCResult.Failure("Second")

        val f0 = flowOf(failure1)
        val f1 = flowOf(failure2)

        val result = combineUCResults(
            f0, f1,
            onFirstFailure = { it.userError }
        ) { _, _ -> "OK" }
            .first()

        assertThat(result).isEqualTo("First")
    }

    @Test
    fun `3-flow combine transforms correctly`() = runTest {
        val f0 = flowOf(UCResult.Success(1))
        val f1 = flowOf(UCResult.Success(2))
        val f2 = flowOf(UCResult.Success(3))

        val result = combineUCResults(
            f0, f1, f2,
            onFirstFailure = { -1 }
        ) { a, b, c -> a + b + c }
            .first()

        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `4-flow combine transforms correctly`() = runTest {
        val result = combineUCResults(
            flowOf(UCResult.Success(1)),
            flowOf(UCResult.Success(2)),
            flowOf(UCResult.Success(3)),
            flowOf(UCResult.Success(4)),
            onFirstFailure = { -1 }
        ) { a, b, c, d -> a + b + c + d }
            .first()

        assertThat(result).isEqualTo(10)
    }

    @Test
    fun `5-flow combine transforms correctly`() = runTest {
        val result = combineUCResults(
            flowOf(UCResult.Success(1)),
            flowOf(UCResult.Success(1)),
            flowOf(UCResult.Success(1)),
            flowOf(UCResult.Success(1)),
            flowOf(UCResult.Success(1)),
            onFirstFailure = { -1 }
        ) { a, b, c, d, e -> a + b + c + d + e }
            .first()

        assertThat(result).isEqualTo(5)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `combine reacts to changes and propagates first failure`() = runTest {
        val f0 = MutableStateFlow<UCResult<Int>>(UCResult.Success(1))
        val f1 = MutableStateFlow<UCResult<Int>>(UCResult.Success(2))

        val combined = combineUCResults(
            f0, f1,
            onFirstFailure = { -1 }
        ) { a, b -> a + b }

        val emissions = mutableListOf<Int>()
        val job = launch {
            combined.collect { emissions.add(it) }
        }

        advanceUntilIdle()

        f1.value = UCResult.Failure("Fail")

        advanceUntilIdle()

        job.cancel()

        assertThat(emissions.first()).isEqualTo(3)
        assertThat(emissions.last()).isEqualTo(-1)
    }
}