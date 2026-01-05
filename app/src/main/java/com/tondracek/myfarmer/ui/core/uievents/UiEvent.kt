package com.tondracek.myfarmer.ui.core.uievents

sealed interface UiEvent {
    data class ShowError(val message: String) : UiEvent
}
