package com.tondracek.myfarmer.ui.core.navigation

import androidx.navigation.NavController
import javax.inject.Inject

typealias Route = String

class AppNavigator @Inject constructor() {

    lateinit var navController: NavController

    fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    fun <T : Any> navigate(route: T) {
        navController.navigate(route = route)
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}