package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.AppUiStateScope
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController

inline fun <reified T : Route> NavGraphBuilder.routeDestination(
    crossinline uiConfig: @Composable AppUiStateScope.() -> Unit = { },
    crossinline content: @Composable (appUiController: AppUiController) -> Unit
) = composable<T> {
    val appUiController = LocalAppUiController.current

    val appUiStateScope = AppUiStateScope().apply { uiConfig() }
    val appUiState by appUiStateScope.appUiState.collectAsState()

    LaunchedEffect(appUiState) {
        appUiController.apply(appUiState)
    }

    content(appUiController)
}
