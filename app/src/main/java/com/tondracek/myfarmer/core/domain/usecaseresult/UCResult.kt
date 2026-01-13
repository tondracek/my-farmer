package com.tondracek.myfarmer.core.domain.usecaseresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult.Failure
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

sealed interface UCResult<out T> {

    data class Success<T>(val data: T) : UCResult<T>

    data class Failure(
        val error: DomainError,
        val cause: Throwable? = null
    ) : UCResult<Nothing>

    suspend fun withSuccess(block: suspend (T) -> Unit): UCResult<T> = apply {
        if (this is Success) block(this.data)
    }

    suspend fun withFailure(block: suspend (Failure) -> Unit): UCResult<T> =
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

    fun <R> fold(onSuccess: (T) -> R, onFailure: (Failure) -> R) = when (this) {
        is Success -> onSuccess(data)
        is Failure -> onFailure(this)
    }

    companion object {

        /**
         * TODO: remove and use try-catch
         * Executes the given [block] and wraps its result into [UCResult].
         *
         * The [errorMappings] are evaluated ⚠️**in order**⚠️; the first matching exception
         * type (using `is` / `isInstance`) is used. More specific exception types must
         * therefore come **before** more general ones.
         *
         * Coroutine cancellation is respected: [CancellationException] is rethrown
         * and never wrapped into [UCResult.Failure].
         *
         * @param errorMappings ordered pairs of exception types to corresponding domain errors
         * @param defaultError domain error used when no mapping matches
         * @param block suspend operation that may throw
         */
        inline fun <T> of(
            defaultError: DomainError,
            vararg errorMappings: Pair<KClass<out Throwable>, DomainError>,
            block: () -> T,
        ): UCResult<T> = try {
            Success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            val domainError = errorMappings
                .firstOrNull { (klass, _) -> klass.isInstance(e) }
                ?.second
                ?: defaultError

            Failure(domainError, e)
        }
    }
}

inline fun <T, R> UCResult<T>.mapSuccess(transform: (T) -> R): UCResult<R> = when (this) {
    is Success -> Success(transform(data))
    is Failure -> this
}

fun <T> List<UCResult<T>>.toUCResultList(): UCResult<List<T>> {
    val resultList = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Success -> resultList.add(result.data)
            is Failure -> return result
        }
    }
    return Success(resultList)
}

inline fun <T, R> UCResult<T>.mapFlatten(transform: (T) -> UCResult<R>): UCResult<R> = when (this) {
    is Success -> transform(data)
    is Failure -> this
}


fun <T> UCResult<T>.log() = this.also {
    when (it) {
        is Success -> Timber.d("UCResult Success: ${it.data}")
        is Failure -> Timber.e(it.cause, "UCResult Failure: ${it.error}")
    }
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

fun <T> Flow<UCResult<T>>.getOrElse(defaultValue: T): Flow<T> = this.map { result ->
    result.getOrElse(defaultValue)
}

fun <T> Flow<UCResult<T>>.withFailure(
    onError: suspend (Failure) -> Unit
): Flow<UCResult<T>> = this.onEach { result ->
    result.withFailure { onError(it) }
}

inline fun <T, R> Flow<UCResult<T>>.mapFlow(crossinline transform: (T) -> R): Flow<UCResult<R>> =
    this.map { result -> result.mapSuccess(transform) }

inline fun <T, R> Flow<UCResult<T>>.mapFlowUC(crossinline transform: (T) -> UCResult<R>): Flow<UCResult<R>> =
    this.map { result -> result.mapFlatten(transform) }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<UCResult<T>>.flatMap(transform: suspend (T) -> Flow<UCResult<R>>): Flow<UCResult<R>> =
    this.flatMapLatest { result ->
        when (result) {
            is Success -> transform(result.data)
            is Failure -> flowOf(result)
        }
    }

fun <T, R> UCResult<T>.flatMap(transform: (T) -> Flow<UCResult<R>>): Flow<UCResult<R>> =
    when (this) {
        is Success -> transform(this.data)
        is Failure -> flowOf(this)
    }
