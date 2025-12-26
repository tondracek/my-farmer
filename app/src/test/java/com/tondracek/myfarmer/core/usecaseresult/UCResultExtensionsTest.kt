package com.tondracek.myfarmer.core.usecaseresult

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.util.setupTimberForTests
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UCResultExtensionsTest {

    @Before
    fun setup() {
        setupTimberForTests()
    }

    @Test
    fun `getOrReturn returns data on Success`() {
        val result = UCResult.Success(10)

        val value = result.getOrReturn { error("Should not run") }

        assertThat(value).isEqualTo(10)
    }

    @Test(expected = IllegalStateException::class)
    fun `getOrReturn executes block on Failure`() {
        val result = UCResult.Failure("User error")

        result.getOrReturn { throw IllegalStateException(it.userError) }
    }

    @Test
    fun `getOrElse value returns data on Success`() {
        val result = UCResult.Success("OK")

        val value = result.getOrElse("Default")

        assertThat(value).isEqualTo("OK")
    }

    @Test
    fun `getOrElse value returns default on Failure`() {
        val result = UCResult.Failure("Error")

        val value = result.getOrElse("Default")

        assertThat(value).isEqualTo("Default")
    }

    @Test
    fun `getOrElse lambda returns data on Success`() {
        val result = UCResult.Success(5)

        var failure: UCResult.Failure? = null
        val value = result.getOrElse {
            failure = it
            -999
        }

        assertThat(value).isEqualTo(5)
        assertThat(failure).isNull()
    }

    @Test
    fun `getOrElse lambda transforms failure`() {
        val result = UCResult.Failure("Error", "System")

        var failure: UCResult.Failure? = null
        val value = result.getOrElse {
            failure = it
            -999
        }

        assertThat(value).isEqualTo(-999)
        assertThat(failure).isEqualTo(result)
    }

    @Test
    fun `mapFlowUCSuccess maps Success values`() = runTest {
        val flow = flowOf(UCResult.Success(2))

        val mapped = flow.mapFlowUCSuccess { it * 3 }.first()

        assertThat((mapped as UCResult.Success).data).isEqualTo(6)
    }

    @Test
    fun `mapFlowUCSuccess passes through Failure`() = runTest {
        val failure = UCResult.Failure("Oops")
        val flow: Flow<UCResult<*>> = flowOf(failure)

        val mapped = flow.mapFlowUCSuccess { it.toString() }.first()

        assertThat(mapped).isEqualTo(failure)
    }

    @Test
    fun `mapFlowUCSuccessFlat maps Success values`() = runTest {
        val flow = flowOf(UCResult.Success(2))

        val mapped = flow.mapFlowUCSuccessFlat { UCResult.Success("Hello") }.first()

        assertThat((mapped as UCResult.Success).data).isEqualTo("Hello")
    }

    @Test
    fun `mapFlowUCSuccessFlat passes through Failure`() = runTest {
        val failure = UCResult.Failure("Oops")
        val flow: Flow<UCResult<*>> = flowOf(failure)

        val mapped = flow.mapFlowUCSuccessFlat { UCResult.Success("Hello") }.first()

        assertThat(mapped).isEqualTo(failure)
    }

    @Test
    fun `flatMapSuccess maps Success into a new flow of Success`() = runTest {
        val flow = flowOf(UCResult.Success(3))

        val output = flow.flatMapSuccess { value ->
            flowOf(value * 4)
        }.first()

        assertThat((output as UCResult.Success).data).isEqualTo(12)
    }

    @Test
    fun `flatMapSuccess returns Failure immediately`() = runTest {
        val failure = UCResult.Failure("Broken")
        val flow = flowOf(failure)

        val output = flow.flatMapSuccess { flowOf(99) }.first()

        assertThat(output).isEqualTo(failure)
    }

    @Test
    fun `flatMapSuccess supports multiple emissions`() = runTest {
        val flow = flowOf(UCResult.Success(2))

        val output = flow.flatMapSuccess { value ->
            flowOf(value, value + 1, value + 2)
        }.toList()

        assertThat(output.map { (it as UCResult.Success).data })
            .containsExactly(2, 3, 4)
            .inOrder()
    }
}
