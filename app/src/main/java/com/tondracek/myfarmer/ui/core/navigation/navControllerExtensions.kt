package com.tondracek.myfarmer.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.json.Json

val navigationJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun NavController.navigateToGraph(navGraph: NavGraph, saveState: Boolean = true) {
    val navController = this
    println("XXX Navigating to graph: $navGraph")
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

inline fun <reified T> NavController.navigateForResult(
    route: Route,
    key: String,
    crossinline onResult: (T) -> Unit
) {
    val navController = this
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.remove<String>(key)

    val liveData = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>(key)

    liveData?.observeForever(object : Observer<String> {
        override fun onChanged(value: String) {
            liveData.removeObserver(this)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(key)

            val result: T = navigationJson.decodeFromString(value)
            onResult(result)
        }
    })

    navController.navigate(route)
}

inline fun <reified T> NavController.setResult(key: String, value: T) {
    val jsonString = navigationJson.encodeToString(value)
    this.previousBackStackEntry
        ?.savedStateHandle
        ?.set(key, jsonString)
}