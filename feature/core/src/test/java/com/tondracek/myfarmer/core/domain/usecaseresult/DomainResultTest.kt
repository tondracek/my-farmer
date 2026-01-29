package com.tondracek.myfarmer.core.domain.usecaseresult

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DomainResultTest {

    @Test
    fun `isSuccess returns true only for Success`() {
        assertThat(DomainResult.Success(1).isSuccess()).isTrue()
        assertThat(DomainResult.Failure(AuthError.Unknown).isSuccess()).isFalse()
    }

    @Test
    fun `getOrNull returns data for Success and null for Failure`() {
        assertThat(DomainResult.Success("ok").getOrNull()).isEqualTo("ok")
        val result: DomainResult<Int> = DomainResult.Failure(AuthError.Unknown)
        assertThat(result.getOrNull()).isEqualTo(null)
    }

    @Test
    fun `getOrElse returns data or default`() {
        assertThat(
            DomainResult.Success(10).getOrElse(0)
        ).isEqualTo(10)

        assertThat(
            DomainResult.Failure(AuthError.Unknown).getOrElse(0)
        ).isEqualTo(0)
    }

    @Test
    fun `withSuccess executes block only on Success`() = runTest {
        var called = false

        DomainResult.Success(5).withSuccess {
            called = true
        }

        assertThat(called).isTrue()

        called = false

        DomainResult.Failure(AuthError.Unknown).withSuccess {
            called = true
        }

        assertThat(called).isFalse()
    }

    @Test
    fun `withFailure executes block only on Failure`() = runTest {
        var called = false

        DomainResult.Failure(AuthError.Unknown).withFailure {
            called = true
        }

        assertThat(called).isTrue()

        called = false

        DomainResult.Success(1).withFailure {
            called = true
        }

        assertThat(called).isFalse()
    }

    @Test
    fun `fold executes correct branch`() = runTest {
        val success = DomainResult.Success(2).fold(
            onSuccess = { it * 2 },
            onFailure = { -1 }
        )

        val failure = DomainResult.Failure(AuthError.Unknown).fold(
            onSuccess = { it },
            onFailure = { -1 }
        )

        assertThat(success).isEqualTo(4)
        assertThat(failure).isEqualTo(-1)
    }

    @Test
    fun `mapSuccess transforms data only for Success`() {
        val success = DomainResult.Success(2).mapSuccess { it * 2 }
        val failure = DomainResult.Failure(AuthError.Unknown).mapSuccess { 0 }

        assertThat(success).isEqualTo(DomainResult.Success(4))
        assertThat(failure).isInstanceOf(DomainResult.Failure::class.java)
    }

    @Test
    fun `mapFlatten flattens nested DomainResult`() {
        val success = DomainResult.Success(2).mapFlatten {
            DomainResult.Success(it * 2)
        }

        val failure = DomainResult.Failure(AuthError.Unknown).mapFlatten {
            DomainResult.Success(0)
        }

        assertThat(success).isEqualTo(DomainResult.Success(4))
        assertThat(failure).isInstanceOf(DomainResult.Failure::class.java)
    }

    @Test(expected = Exception::class)
    fun `getOrReturn returns data or throws`() {
        val value = DomainResult.Success(1).getOrReturn {
            error("should not happen")
        }

        assertThat(value).isEqualTo(1)

        DomainResult.Failure(AuthError.Unknown).getOrReturn {
            error("boom")
        }
    }

    @Test
    fun `toResultList returns list when all succeed`() {
        val list = listOf(
            DomainResult.Success(1),
            DomainResult.Success(2),
            DomainResult.Success(3)
        )

        val result = list.toResultList()

        assertThat(result).isEqualTo(DomainResult.Success(listOf(1, 2, 3)))
    }

    @Test
    fun `toResultList returns first failure`() {
        val failure = DomainResult.Failure(AuthError.Unknown)

        val list = listOf(
            DomainResult.Success(1),
            failure,
            DomainResult.Success(3)
        )

        val result = list.toResultList()

        assertThat(result).isEqualTo(failure)
    }

    @Test
    fun `Flow getOrElse unwraps values`() = runTest {
        val flow = flowOf(
            DomainResult.Success(1),
            DomainResult.Failure(AuthError.Unknown)
        )

        val result = flow.getOrElse(0).toList()

        assertThat(result).containsExactly(1, 0).inOrder()
    }

    @Test
    fun `withFailure invokes callback on failure`() = runTest {
        var called = false

        val flow = flowOf(
            DomainResult.Success(1),
            DomainResult.Failure(AuthError.Unknown)
        ).withFailure {
            called = true
        }

        // trigger collection
        flow.toList()

        assertThat(called).isTrue()
    }

    @Test
    fun `mapFlow maps success values`() = runTest {
        val flow = flowOf(
            DomainResult.Success(2)
        )

        val result = flow.mapFlow { it * 2 }.first()

        assertThat(result).isEqualTo(DomainResult.Success(4))
    }

    @Test
    fun `mapFlowUC flattens nested DomainResult in flow`() = runTest {
        val flow = flowOf(
            DomainResult.Success(2)
        )

        val result = flow.mapFlowUC {
            DomainResult.Success(it * 2)
        }.first()

        assertThat(result).isEqualTo(DomainResult.Success(4))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flatMap switches on success and propagates failure`() = runTest {
        val successFlow = flowOf(DomainResult.Success(1))
        val failureFlow = flowOf(DomainResult.Failure(AuthError.Unknown))

        val successResult = successFlow.flatMap {
            flowOf(DomainResult.Success(it + 1))
        }.first()

        val failureResult = failureFlow.flatMap {
            flowOf(DomainResult.Success(0))
        }.first()

        assertThat(successResult).isEqualTo(DomainResult.Success(2))
        assertThat(failureResult).isInstanceOf(DomainResult.Failure::class.java)
    }
}
