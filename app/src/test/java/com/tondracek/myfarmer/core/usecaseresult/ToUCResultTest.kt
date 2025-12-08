package com.tondracek.myfarmer.core.usecaseresult


import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.util.setupTimberForTests
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UCResultConversionTest {

    @Before
    fun setup() {
        setupTimberForTests()
    }

    // ---------------------------
    // toUCResult (userError)
    // ---------------------------

    @Test
    fun `toUCResult returns Success on normal execution`() {
        val result = toUCResult { 42 }

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat((result as UCResult.Success).data).isEqualTo(42)
    }

    @Test
    fun `toUCResult returns Failure on exception`() {
        val result = toUCResult("Boom!") {
            error("Oops")
        }

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
        val failure = result as UCResult.Failure

        assertThat(failure.userError).isEqualTo("Boom!")
        assertThat(failure.systemError).contains("Oops")
    }

    @Test
    fun `toUCResult returns Failure on exception with default text`() {
        val result = toUCResult("Boom!") {
            throw Throwable()
        }

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
        val failure = result as UCResult.Failure

        assertThat(failure.userError).isEqualTo("Boom!")
        assertThat(failure.systemError).isNotEmpty()
    }

    // ---------------------------
    // toUCResult (explicit Failure)
    // ---------------------------

    @Test
    fun `toUCResult failure version returns Success when block succeeds`() {
        val fallback = UCResult.Failure("Fallback")

        val result = toUCResult(fallback) { 123 }

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat((result as UCResult.Success).data).isEqualTo(123)
    }

    @Test
    fun `toUCResult failure version returns provided Failure on exception`() {
        val fallback = UCResult.Failure("Provided")

        val result = toUCResult(fallback) { throw IllegalStateException("Boom") }

        assertThat(result).isEqualTo(fallback)
    }

    // ---------------------------
    // Flow<T>.toUCResult(userError)
    // ---------------------------

    @Test
    fun `Flow toUCResult maps values to Success`() = runTest {
        val flow = flowOf(10, 20, 30).toUCResult()

        val result = flow.first() as UCResult.Success
        assertThat(result.data).isEqualTo(10)
    }

    @Test
    fun `Flow toUCResult catches exception and emits Failure`() = runTest {
        val flow = flow {
            emit(1)
            throw IllegalArgumentException("Bad!")
        }.toUCResult("Error happened")

        val items = flow.toList()

        assertThat(items[0]).isInstanceOf(UCResult.Success::class.java)
        assertThat(items[1]).isInstanceOf(UCResult.Failure::class.java)

        val failure = items[1] as UCResult.Failure
        assertThat(failure.userError).isEqualTo("Error happened")
        assertThat(failure.systemError).contains("Bad!")
    }

    // ---------------------------
    // Flow<T>.toUCResult(failure)
    // ---------------------------

    @Test
    fun `Flow toUCResult(failure) emits Success values`() = runTest {
        val failure = UCResult.Failure("Fallback")

        val flow = flowOf("A").toUCResult(failure)
        val result = flow.first()

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat((result as UCResult.Success).data).isEqualTo("A")
    }

    @Test
    fun `Flow toUCResult(failure) emits provided Failure on exception`() = runTest {
        val fallback = UCResult.Failure("Provided")

        val flow = flow<String> {
            emit("Ok")
            throw RuntimeException("Boom")
        }.toUCResult(fallback)

        val items = flow.toList()

        assertThat(items[0]).isInstanceOf(UCResult.Success::class.java)
        assertThat(items[1]).isEqualTo(fallback)
    }
}
