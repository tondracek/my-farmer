package com.tondracek.myfarmer.ui.core.navigation

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor(
    val json: Json
) {

    lateinit var navController: NavController

    fun navigate(route: Route) = navController.navigate(route)

    fun getCurrentRoute(): Flow<Route?> = navController
        .currentBackStackEntryFlow
        .map {
            Route.getRouteClass(it)
                ?.let { routeClass -> it.toRoute(routeClass) }
        }

    fun navigateBack() = navController.navigateUp()

    inline fun <reified T> navigateForResult(
        route: Route,
        key: String,
        crossinline onResult: (T) -> Unit
    ) {
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

                val result: T = json.decodeFromString(value)
                onResult(result)
            }
        })

        navController.navigate(route)
    }
}

val navigationJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
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