package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
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

    val appUiStateScope = AppUiStateScope(appUiController)
    appUiStateScope.uiConfig()
    appUiStateScope.apply()

    content(appUiController)
}
