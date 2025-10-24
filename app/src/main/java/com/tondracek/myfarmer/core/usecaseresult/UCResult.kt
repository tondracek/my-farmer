package com.tondracek.myfarmer.core.usecaseresult

sealed interface UCResult<out T> {

    data class Success<T>(val data: T) : UCResult<T>

    open class Failure(val userError: String, val systemError: String? = null) :
        UCResult<Nothing>

    fun <R> mapSuccess(transform: (T) -> R): UCResult<R> = when (this) {
        is Success -> Success(transform(data))
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
}

/**
 * - For `UseCaseResult.Success` returns the `data` value
 * - For `UseCaseResult.Failure` applies the `block` function that can return some value
 */
inline fun <T> UCResult<T>.getOrReturn(block: (UCResult.Failure) -> Nothing): T =
    when (this) {
        is UCResult.Success -> data
        is UCResult.Failure -> block(this)
    }