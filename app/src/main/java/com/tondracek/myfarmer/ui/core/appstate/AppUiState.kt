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

    private val _state = MutableStateFlow(AppUiState())
    val state: StateFlow<AppUiState> = _state

    fun updateTitle(title: String?) = _state.update {
        it.copy(title = title)
    }

    fun updateTopBarPadding(applyPadding: Boolean) = _state.update {
        it.copy(applyTopBarPadding = applyPadding)
    }

    fun updateShowTopBar(showTopBar: Boolean) = _state.update {
        it.copy(showTopBar = showTopBar)
    }

    fun apply(uiState: AppUiState) = _state.update { uiState }

    private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errors = _errors.asSharedFlow()

    fun showError(message: String) {
        _errors.tryEmit(message)
    }
}

data class AppUiState(
    val title: String? = null,
    val applyTopBarPadding: Boolean = true,
    val showTopBar: Boolean = true,
)

class AppUiStateScope(
    private val controller: AppUiController
) {
    var appUiState: MutableStateFlow<AppUiState> = MutableStateFlow(AppUiState())

    var title: String?
        get() = title
        set(value) = appUiState.update { it.copy(title = value) }

    var applyTopBarPadding: Boolean
        get() = applyTopBarPadding
        set(value) = appUiState.update { it.copy(applyTopBarPadding = value) }

    var showTopBar: Boolean
        get() = showTopBar
        set(value) = appUiState.update { it.copy(showTopBar = value) }

    fun apply() = controller.apply(appUiState.value)
}

suspend fun AppUiController.collectEvents(events: Flow<UiEvent>) {
    events.collect { event ->
        when (event) {
            is UiEvent.ShowError -> this.showError(event.message)
        }
    }
}