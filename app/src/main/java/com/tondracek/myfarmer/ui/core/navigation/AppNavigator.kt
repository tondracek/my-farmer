package com.tondracek.myfarmer.ui.core.navigation

import androidx.navigation.NavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppNavigator @Inject constructor() {

    lateinit var navController: NavController

    fun navigate(route: Route) = navController.navigate(route)

    fun getCurrentRoute(): Flow<Route?> = navController
        .currentBackStackEntryFlow
        .map {
            Route.getRouteClass(it)
                ?.let { routeClass -> it.toRoute(routeClass) }
        }

    fun navigateBack() = navController.navigateUp()
}
