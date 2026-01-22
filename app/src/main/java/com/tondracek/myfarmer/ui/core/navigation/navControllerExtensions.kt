package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import timber.log.Timber

fun NavController.navigateToGraph(navGraph: NavGraph, saveState: Boolean = true) {
    val navController = this
    Timber.d("Navigating to graph: $navGraph")
    navController.navigate(navGraph) {
        popUpTo(navController.graph.startDestinationId) { this.saveState = saveState }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun NavController.isInNavGraph(navGraph: NavGraph): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()

    return navBackStackEntry
        ?.destination
        ?.hierarchy
        ?.any { it.hasRoute(navGraph::class) }
        ?: false
}
