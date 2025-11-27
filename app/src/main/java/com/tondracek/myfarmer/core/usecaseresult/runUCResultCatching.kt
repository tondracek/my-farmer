package com.tondracek.myfarmer.core.usecaseresult

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

inline fun <T> runCatchingUCResult(
    userError: String = "An error occurred...",
    block: () -> T,
): UCResult<T> = runCatching { block() }
    .fold(
        onSuccess = { UCResult.Success(it) },
        onFailure = {
            UCResult.Failure(
                userError = userError,
                systemError = it.message ?: "runUCResultCatching - An error occurred.",
            )
        }
    )

inline fun <T> runCatchingUCResultFlow(
    userError: String = "An error occurred...",
    block: () -> Flow<T>,
): Flow<UCResult<T>> = runCatching { block() }
    .fold(
        onSuccess = { it.map { result -> UCResult.Success(result) } },
        onFailure = {
            flowOf(
                UCResult.Failure(
                    userError = if (it is UCThrowable) it.userError else userError,
                    systemError = it.message ?: "runUCResultCatching - An error occurred.",
                )
            )
        }
    )

data class UCThrowable(
    val userError: String,
    val systemError: String
) : Throwable(
    message = systemError
)