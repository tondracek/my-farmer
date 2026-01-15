package com.tondracek.myfarmer.core.domain.usecaseresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Extension function to convert a Flow<T> into a Flow<UCResult<T>>.
 * It maps successful emissions to UCResult.Success and catches exceptions,
 * emitting UCResult.Failure with the provided DomainError.
 *
 * @param error The DomainError to use in case of failure.
 * @return A Flow emitting UCResult<T> instances.
 */
fun <T> Flow<T>.toUCResult(
    error: DomainError
): Flow<DomainResult<T>> =
    this.map { DomainResult.Success(it) as DomainResult<T> }
        .catch { e ->
            Timber.e(e, "Error in toUCResult flow extension")
            emit(DomainResult.Failure(error, e))
        }

fun <T> Flow<T?>.toUCResultNonNull(
    nullError: DomainError,
    defaultError: DomainError
): Flow<DomainResult<T>> =
    this.map {
        when (it) {
            null -> DomainResult.Failure(nullError)
            else -> DomainResult.Success(it)
        }
    }.catch { e ->
        Timber.e(e, "Error in toUCResultNonNull flow extension")
        emit(DomainResult.Failure(defaultError, e))
    }
