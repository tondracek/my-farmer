package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val LocalAppNavigator = compositionLocalOf<AppNavigator> {
    error("AppNavigator not provided")
}


class AppNavigator @Inject constructor() {

    lateinit var navController: NavController

    fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    fun getCurrentRoute(): Flow<String?> =
        navController.currentBackStackEntryFlow.map { it.destination.route }

    fun <T : Any> navigate(route: T) {
        navController.navigate(route = route)
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}
