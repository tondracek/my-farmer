package com.tondracek.myfarmer.core.usecaseresult

sealed interface UseCaseResult<out T> {

    data class Success<T>(val data: T) : UseCaseResult<T>

    open class Failure(val userError: String, val systemError: String? = null) :
        UseCaseResult<Nothing>

    fun <R> withSuccess(block: (T) -> R): UseCaseResult<R> {
        return when (this) {
            is Success -> Success(block(data))
            is Failure -> this
        }
    }

    fun withFailure(block: (Failure) -> Unit): UseCaseResult<T> = apply {
        if (this is Failure) block(this)
    }

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }
}

inline fun <T> UseCaseResult<T>.getOrReturn(block: (UseCaseResult.Failure) -> Nothing): T =
    when (this) {
        is UseCaseResult.Success -> data
        is UseCaseResult.Failure -> block(this)
    }