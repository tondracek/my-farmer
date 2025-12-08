package com.tondracek.myfarmer.core.usecaseresult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Executes the given [block] and wraps its result in a [UCResult].
 * If the block executes successfully, it returns [UCResult.Success] with the result.
 * If an exception is thrown, it returns [UCResult.Failure] with the provided
 * [userError] message and the system error message from the exception.
 *
 * @param userError The user-friendly error message to use in case an exception occurs.
 * @param block The block of code to execute.
 * @return A [UCResult] representing the outcome of the block execution.
 */
inline fun <T> toUCResult(
    userError: String = "Unexpected error occurred.",
    block: () -> T,
): UCResult<T> =
    runCatching { block() }.fold(
        onSuccess = { UCResult.Success(it) },
        onFailure = { e -> UCResult.Failure(userError = userError, throwable = e) }
    )

inline fun <T> toUCResult(failure: UCResult.Failure, block: () -> T): UCResult<T> =
    runCatching { block() }.fold(
        onSuccess = { UCResult.Success(it) },
        onFailure = { e -> failure }
    )

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
