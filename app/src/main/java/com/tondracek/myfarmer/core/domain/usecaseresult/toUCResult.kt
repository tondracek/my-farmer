package com.tondracek.myfarmer.core.domain.usecaseresult

import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

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
): Flow<UCResult<T>> =
    this.map { UCResult.Success(it) as UCResult<T> }
        .catch { e -> emit(UCResult.Failure(error, e)) }

fun <T> Flow<T?>.toUCResultNonNull(
    nullError: DomainError,
    defaultError: DomainError
): Flow<UCResult<T>> =
    this.map {
        when (it) {
            null -> UCResult.Failure(nullError)
            else -> UCResult.Success(it)
        }
    }.catch { e -> emit(UCResult.Failure(defaultError, e)) }
