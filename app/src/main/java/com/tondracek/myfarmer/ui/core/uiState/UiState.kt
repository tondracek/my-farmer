package com.tondracek.myfarmer.ui.core.uiState

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

sealed interface UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>
    data object Loading : UiState<Nothing>
}

inline fun <T> UiState<T>.getOrReturn(block: () -> Nothing): T = when (this) {
    is UiState.Success<T> -> this.data
    UiState.Loading -> block()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<UCResult<T>>.asUiState(
    defaultValue: T,
    onError: suspend (UCResult.Failure) -> Unit = {},
): Flow<UiState<T>> = transformLatest { result ->
    emit(UiState.Loading)

    when (result) {
        is UCResult.Success -> emit(UiState.Success(result.data))
        is UCResult.Failure -> {
            onError(result)
            emit(UiState.Success(defaultValue))
        }
    }
}