package com.tondracek.myfarmer.core.usecaseresult

import com.tondracek.myfarmer.core.usecaseresult.UCResult.Failure
import com.tondracek.myfarmer.core.usecaseresult.UCResult.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber

sealed interface UCResult<out T> {

    data class Success<T>(val data: T) : UCResult<T>

    open class Failure(val userError: String, val systemError: String? = null) : UCResult<Nothing> {
        constructor(userError: String, throwable: Throwable) : this(
            userError,
            throwable.message ?: "Unknown error $throwable"
        ) {
            Timber.e(throwable)
        }

        init {
            logFailure()
        }
    }

    fun <R> mapSuccess(transform: (T) -> R): UCResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Failure -> this
    }

    fun <R> mapSuccessFlat(transform: (T) -> UCResult<R>): UCResult<R> = when (this) {
        is Success -> transform(data)
        is Failure -> this
    }

    suspend fun withSuccess(block: suspend (T) -> Unit): UCResult<T> = apply {
        if (this is Success) block(this.data)
    }

    suspend fun withFailure(block: suspend (Failure) -> Unit): UCResult<T> = apply {
        if (this is Failure) block(this)
    }

    /**
     * - For `UseCaseResult.Success` returns the `data` value
     * - For `UseCaseResult.Failure` returns `null`
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun <R> fold(onSuccess: (T) -> R, onFailure: (Failure) -> R) = when (this) {
        is Success -> onSuccess(data)
        is Failure -> onFailure(this)
    }

    companion object {
        fun <T> of(data: T): UCResult<T> = Success(data)

        inline fun <T> of(
            userError: String = "Unexpected error occurred.",
            block: () -> T,
        ): UCResult<T> =
            runCatching { block() }.fold(
                onSuccess = { Success(it) },
                onFailure = { Failure(userError = userError, throwable = it) }
            )

        inline fun <T> of(failure: Failure, block: () -> T): UCResult<T> =
            runCatching { block() }.fold(
                onSuccess = { Success(it) },
                onFailure = { Failure(failure.userError, it) }
            )
    }
}

private fun Failure.logFailure() {
    val border = "========================================="

    Timber.e(
        """
            $border
            ❌ FAILURE in ${this.javaClass.simpleName}
            ❌ USER ERROR: $userError
            ❌ SYSTEM ERROR: ${systemError ?: "null"}
            $border
        """.trimIndent()
    )
}

/**
 * - For `UseCaseResult.Success` returns the `data` value
 * - For `UseCaseResult.Failure` applies the `block` function that can return some value
 */
inline fun <T> UCResult<T>.getOrReturn(block: (Failure) -> Nothing): T =
    when (this) {
        is Success -> data
        is Failure -> block(this)
    }

fun <T> UCResult<T>.getOrElse(defaultValue: T): T = when (this) {
    is Success -> data
    is Failure -> defaultValue
}

inline fun <T> UCResult<T>.getOrElse(defaultValue: (Failure) -> T): T = when (this) {
    is Success -> data
    is Failure -> defaultValue(this)
}

fun <T> Flow<UCResult<T>>.getOrElse(
    defaultValue: T,
    onError: suspend (Failure) -> Unit = {}
): Flow<T> = this.map { result ->
    result.getOrElse {
        onError(it)
        defaultValue
    }
}

fun <T, R> Flow<UCResult<T>>.mapSuccessFlow(transform: suspend (T) -> R): Flow<UCResult<R>> =
    this.map { result ->
        when (result) {
            is Success -> UCResult.of { transform(result.data) }
            is Failure -> result
        }
    }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<UCResult<T>>.flatMapSuccess(transform: (T) -> Flow<UCResult<R>>): Flow<UCResult<R>> =
    this.flatMapLatest { result ->
        when (result) {
            is Success -> transform(result.data)
            is Failure -> flowOf(result)
        }
    }