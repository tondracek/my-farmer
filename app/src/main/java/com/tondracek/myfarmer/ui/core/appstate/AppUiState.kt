package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.runtime.compositionLocalOf
import com.tondracek.myfarmer.ui.core.uievents.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

val LocalAppUiController = compositionLocalOf<AppUiController> {
    error("AppUiController not provided")
}

class AppUiController {

    private val _state = MutableStateFlow(AppUiState.Initial)
    val state: StateFlow<AppUiState> = _state

    private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errors = _errors.asSharedFlow()

    fun showError(message: String) {
        _errors.tryEmit(message)
    }

    fun updateTitle(title: String): AppUiController = apply {
        _state.update { it.copy(title = title) }
    }

    fun updateTopBarPadding(applyPadding: Boolean): AppUiController = apply {
        _state.update { it.copy(applyTopBarPadding = applyPadding) }
    }

    fun apply(appUiState: AppUiState) = apply {
        _state.update {
            it.copy(
                title = appUiState.title,
                applyTopBarPadding = appUiState.applyTopBarPadding,
                showTopBar = appUiState.showTopBar,
            )
        }
    }
}

data class AppUiState(
    val title: String?,
    val applyTopBarPadding: Boolean,
    val showTopBar: Boolean,
) {
    companion object {

        val INITIAL_TITLE: String? = null
        const val INITIAL_APPLY_TOP_BAR_PADDING: Boolean = true
        const val INITIAL_SHOW_TOP_BAR: Boolean = true

        val Initial = AppUiState(
            title = INITIAL_TITLE,
            applyTopBarPadding = INITIAL_APPLY_TOP_BAR_PADDING,
            showTopBar = INITIAL_SHOW_TOP_BAR,
        )
    }
}

suspend fun AppUiController.collectEvents(events: Flow<UiEvent>) {
    events.collect { event ->
        when (event) {
            is UiEvent.ShowError -> this.showError(event.message)
        }
    }
}