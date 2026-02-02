package com.tondracek.myfarmer.core.domain.domainresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult.Failure
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed interface DomainResult<out T> {

    data class Success<T>(val data: T) : DomainResult<T>

    data class Failure(
        val error: DomainError,
        val cause: Throwable? = null
    ) : DomainResult<Nothing>

    fun isSuccess(): Boolean = this is Success

    suspend fun withSuccess(block: suspend (T) -> Unit): DomainResult<T> = apply {
        if (this is Success) block(this.data)
    }

    suspend fun withFailure(block: suspend (Failure) -> Unit): DomainResult<T> =
        when (this) {
            is Success -> this
            is Failure -> this.also { block(this) }
        }

    /**
     * - For `UseCaseResult.Success` returns the `data` value
     * - For `UseCaseResult.Failure` returns `null`
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    suspend fun <R> fold(onSuccess: suspend (T) -> R, onFailure: suspend (Failure) -> R) =
        when (this) {
            is Success -> onSuccess(data)
            is Failure -> onFailure(this)
        }
}

inline fun <T, R> DomainResult<T>.mapSuccess(transform: (T) -> R): DomainResult<R> = when (this) {
    is Success -> Success(transform(data))
    is Failure -> this
}

fun <T> List<DomainResult<T>>.toResultList(): DomainResult<List<T>> {
    val resultList = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Success -> resultList.add(result.data)
            is Failure -> return result
        }
    }
    return Success(resultList)
}

inline fun <T, R> DomainResult<T>.mapFlatten(transform: (T) -> DomainResult<R>): DomainResult<R> =
    when (this) {
        is Success -> transform(data)
        is Failure -> this
    }

/**
 * - For `UseCaseResult.Success` returns the `data` value
 * - For `UseCaseResult.Failure` applies the `block` function that can return some value
 */
inline fun <T> DomainResult<T>.getOrReturn(block: (Failure) -> Nothing): T =
    when (this) {
        is Success -> data
        is Failure -> block(this)
    }

fun <T> DomainResult<T>.getOrElse(defaultValue: T): T = when (this) {
    is Success -> data
    is Failure -> defaultValue
}

fun <T> Flow<DomainResult<T>>.getOrElse(defaultValue: T): Flow<T> = this.map { result ->
    result.getOrElse(defaultValue)
}

fun <T> Flow<DomainResult<T>>.withFailure(
    onError: suspend (Failure) -> Unit
): Flow<DomainResult<T>> = this.onEach { result ->
    result.withFailure { onError(it) }
}

inline fun <T, R> Flow<DomainResult<T>>.mapFlow(crossinline transform: (T) -> R): Flow<DomainResult<R>> =
    this.map { result -> result.mapSuccess(transform) }

inline fun <T, R> Flow<DomainResult<T>>.mapFlowUC(crossinline transform: (T) -> DomainResult<R>): Flow<DomainResult<R>> =
    this.map { result -> result.mapFlatten(transform) }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<DomainResult<T>>.flatMap(transform: (T) -> Flow<DomainResult<R>>): Flow<DomainResult<R>> =
    this.flatMapLatest { result ->
        result.flatMap(transform)
    }

fun <T, R> DomainResult<T>.flatMap(transform: (T) -> Flow<DomainResult<R>>): Flow<DomainResult<R>> =
    when (this) {
        is Success -> transform(this.data)
        is Failure -> flowOf(this)
    }
