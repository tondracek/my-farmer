package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.AppUiState
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController

inline fun <reified T : Route> NavGraphBuilder.routeDestination(
    crossinline title: @Composable () -> String? = { AppUiState.INITIAL_TITLE },
    applyTopBarPadding: Boolean = AppUiState.INITIAL_APPLY_TOP_BAR_PADDING,
    showTopBar: Boolean = AppUiState.INITIAL_SHOW_TOP_BAR,
    crossinline content: @Composable (appUiController: AppUiController) -> Unit
) = composable<T> {
    val appUiState = AppUiState(
        title = title(),
        applyTopBarPadding = applyTopBarPadding,
        showTopBar = showTopBar
    )

    val appUiController = LocalAppUiController.current
    LaunchedEffect(Unit) {
        appUiController.apply(appUiState)
    }

    content(appUiController)
}
