package com.tondracek.myfarmer.core.data

import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.domainerror.UserError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.KClass

private val MyDomainError = ShopError.Unknown
private val DefaultError = UserError.Unknown

class DomainResultCatchingTest {

    @Test
    fun `domainResultOf maps thrown exception to provided domain error`() {
        val mapped = domainResultOf(
            DefaultError,
            Pair(IllegalArgumentException::class as KClass<out Throwable>, MyDomainError)
        ) {
            throw IllegalArgumentException("bad")
        }
        assertTrue(mapped is DomainResult.Failure)
        val f = mapped as DomainResult.Failure
        assertEquals(MyDomainError, f.error)
        assertTrue(f.cause is IllegalArgumentException)
    }

    @Test
    fun `domainResultOf with value returns success and with error returns failure`() {
        val s = domainResultOf(5)
        assertTrue(s is DomainResult.Success)
        assertEquals(5, (s as DomainResult.Success).data)

        val f = domainResultOf(MyDomainError)
        assertTrue(f is DomainResult.Failure)
        assertEquals(MyDomainError, (f as DomainResult.Failure).error)
    }

    @Test
    fun `toDomainResult maps emissions to success and exceptions to Failure with provided error`() =
        runTest {
            val okFlow = flowOf(1).toDomainResult(MyDomainError)
            val collectedOk = mutableListOf<DomainResult<Int>>()
            okFlow.collect { collectedOk.add(it) }
            assertEquals(1, collectedOk.size)
            assertTrue(collectedOk[0] is DomainResult.Success)

            val errFlow = flow<Int> { throw RuntimeException("boom") }.toDomainResult(DefaultError)
            val collectedErr = mutableListOf<DomainResult<Int>>()
            errFlow.collect { collectedErr.add(it) }
            assertEquals(1, collectedErr.size)
            assertTrue(collectedErr[0] is DomainResult.Failure)
            assertEquals(DefaultError, (collectedErr[0] as DomainResult.Failure).error)
        }

    @Test
    fun `toDomainResultNonNull emits Failure for null and Success for value and maps exceptions to defaultError`() =
        runTest {
            val nullFlow = flowOf<Int?>(null).toDomainResultNonNull(
                nullError = MyDomainError,
                defaultError = DefaultError
            )
            val collectedNull = mutableListOf<DomainResult<Int>>()
            nullFlow.collect { collectedNull.add(it) }
            assertEquals(1, collectedNull.size)
            assertTrue(collectedNull[0] is DomainResult.Failure)
            assertEquals(MyDomainError, (collectedNull[0] as DomainResult.Failure).error)

            val valFlow = flowOf<Int?>(7).toDomainResultNonNull(
                nullError = MyDomainError,
                defaultError = DefaultError
            )
            val collectedVal = mutableListOf<DomainResult<Int>>()
            valFlow.collect { collectedVal.add(it) }
            assertTrue(collectedVal[0] is DomainResult.Success)
            assertEquals(7, (collectedVal[0] as DomainResult.Success).data)

            val thrownFlow = flow<Int?> { throw IllegalStateException("x") }.toDomainResultNonNull(
                nullError = MyDomainError,
                defaultError = DefaultError
            )
            val collectedThrown = mutableListOf<DomainResult<Int>>()
            thrownFlow.collect { collectedThrown.add(it) }
            assertTrue(collectedThrown[0] is DomainResult.Failure)
            assertEquals(DefaultError, (collectedThrown[0] as DomainResult.Failure).error)
        }

    @Test
    fun `domainResultOf rethrows cancellation exception`() {
        try {
            domainResultOf(DefaultError) {
                throw kotlinx.coroutines.CancellationException("c")
            }
            throw AssertionError("CancellationException should be rethrown")
        } catch (e: kotlinx.coroutines.CancellationException) {
            // expected
        }
    }

    @Test
    fun `domainResultOf failure overload preserves provided cause`() {
        val ex = IllegalStateException("cause")
        val r = domainResultOf(DefaultError, ex)
        assertTrue(r is DomainResult.Failure)
        val f = r as DomainResult.Failure
        assertEquals(DefaultError, f.error)
        assertEquals(ex, f.cause)
    }
}


