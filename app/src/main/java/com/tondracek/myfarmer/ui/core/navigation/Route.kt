package com.tondracek.myfarmer.ui.core.navigation

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
sealed interface Route {

    @Serializable
    data object MainShopsRoute : Route

    @Serializable
    data object MyShopsRoute : Route

    @Serializable
    data object EditProfileScreenRoute : Route

    @Serializable
    data object AuthScreenRoute : Route

    @Serializable
    data class ShopBottomSheetRoute(val shopId: String) : Route

    @Serializable
    data class ShopDetailRoute(val shopId: String) : Route

    companion object {
        private val allRoutes = listOf(
            MainShopsRoute::class,
            MyShopsRoute::class,
            EditProfileScreenRoute::class,
            AuthScreenRoute::class,
            ShopBottomSheetRoute::class,
            ShopDetailRoute::class,
        )

        fun getRouteClass(route: NavBackStackEntry): KClass<out Route>? =
            allRoutes
                .firstOrNull { route.destination.hasRoute(it) }
                .also { if (it == null) Log.e("Route", "Unknown route: $route") }
    }
}