package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.runtime.compositionLocalOf

val LocalAppUiState = compositionLocalOf { AppUiState() }
val LocalAppUiController = compositionLocalOf<AppUiController> {
    error("AppUiController not provided")
}

interface AppUiController {
    fun updateTitle(title: String): AppUiController
    fun updateTopBarPadding(applyPadding: Boolean): AppUiController
}

data class AppUiState(
    val title: String? = null,
    val applyTopBarPadding: Boolean = false,
)