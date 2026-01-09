package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController

inline fun <reified T : Route> NavGraphBuilder.routeDestination(
    crossinline content: @Composable (appUiController: AppUiController) -> Unit
) = composable<T> {
    val appUiController = LocalAppUiController.current

    content(appUiController)
}
