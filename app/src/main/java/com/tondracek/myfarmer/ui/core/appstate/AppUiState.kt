package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

val LocalAppUiController = compositionLocalOf<AppUiController> {
    error("AppUiController not provided")
}

class AppUiController {

    private val _state = MutableStateFlow(AppUiState())
    val state: StateFlow<AppUiState> = _state

    fun updateTitle(title: String): AppUiController = apply {
        _state.update { it.copy(title = title) }
        println("UPDATEEEEEEEEEEEEEEE!!!")
    }

    fun updateTopBarPadding(applyPadding: Boolean): AppUiController = apply {
        _state.update { it.copy(applyTopBarPadding = applyPadding) }
        println("UPDATEEEEEEEEEEEEEEE!")
    }

    fun apply(appUiState: AppUiState) = apply {
        _state.update {
            it.copy(
                title = appUiState.title ?: it.title,
                applyTopBarPadding = appUiState.applyTopBarPadding
            )
        }
    }
}

const val APP_UI_STATE_APPLY_TOP_BAR_PADDING_DEFAULT: Boolean = true

data class AppUiState(
    val title: String? = null,
    val applyTopBarPadding: Boolean = APP_UI_STATE_APPLY_TOP_BAR_PADDING_DEFAULT,
)