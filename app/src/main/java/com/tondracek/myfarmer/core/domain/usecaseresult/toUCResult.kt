package com.tondracek.myfarmer.core.domain.usecaseresult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Extension function to convert a Flow<T> into a Flow<UCResult<T>>.
 * It maps successful emissions to UCResult.Success and catches exceptions,
 * emitting UCResult.Failure with the provided user error message.
 *
 * @param userError The user-friendly error message to use in case an exception occurs.
 * @return A Flow emitting UCResult<T> instances.
 */
fun <T> Flow<T>.toUCResult(
    userError: String = "Unexpected error occurred.",
): Flow<UCResult<T>> =
    this.map { UCResult.Success(it) as UCResult<T> }
        .catch { e -> emit(UCResult.Failure(userError = userError, throwable = e)) }

fun <T> Flow<T>.toUCResult(
    failure: UCResult.Failure,
): Flow<UCResult<T>> =
    this.map { UCResult.Success(it) as UCResult<T> }
        .catch { e -> emit(failure) }
