package com.tondracek.myfarmer.core.data.firestore.domainresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

inline fun <T> domainResultOf(
    defaultError: DomainError,
    vararg errorMappings: Pair<KClass<out Throwable>, DomainError>,
    block: () -> T,
): DomainResult<T> = try {
    DomainResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    val domainError = errorMappings
        .firstOrNull { (klass, _) -> klass.isInstance(e) }
        ?.second
        ?: defaultError

    DomainResult.Failure(domainError, e).also {
        Timber.e(e, "domainResultOf caught an exception, mapped to DomainError: $domainError")
    }
}

fun <T> domainResultOf(value: T): DomainResult<T> =
    DomainResult.Success(value)

fun domainResultOf(error: DomainError, exception: Throwable? = null): DomainResult<Nothing> =
    DomainResult.Failure(error, exception)

/**
 * Extension function to convert a Flow<T> into a Flow<UCResult<T>>.
 * It maps successful emissions to UCResult.Success and catches exceptions,
 * emitting UCResult.Failure with the provided DomainError.
 *
 * @param error The DomainError to use in case of failure.
 * @return A Flow emitting UCResult<T> instances.
 */
fun <T> Flow<T>.toDomainResult(
    error: DomainError
): Flow<DomainResult<T>> =
    this.map { DomainResult.Success(it) as DomainResult<T> }
        .catch { e ->
            Timber.e(e, "Error in toDomainResult flow extension")
            emit(DomainResult.Failure(error, e))
        }

fun <T> Flow<T?>.toDomainResultNonNull(
    nullError: DomainError,
    defaultError: DomainError
): Flow<DomainResult<T>> =
    this.map {
        when (it) {
            null -> DomainResult.Failure(nullError)
            else -> DomainResult.Success(it)
        }
    }.catch { e ->
        Timber.e(e, "Error in toDomainResultNonNull flow extension")
        emit(DomainResult.Failure(defaultError, e))
    }

