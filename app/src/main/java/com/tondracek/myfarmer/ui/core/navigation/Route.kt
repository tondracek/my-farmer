package com.tondracek.myfarmer.ui.core.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable
import timber.log.Timber
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

    @Serializable
    data class ShopReviews(val shopId: String) : Route

    @Serializable
    data object CreateShop : Route

    @Serializable
    data class UpdateShop(val shopId: String) : Route

    @Serializable
    data object AddCategoryDialog : Route

    @Serializable
    data class ShopFilterDialog(val filterRepositoryKey: String) : Route

    companion object {
        private val allRoutes = setOf(
            MainShopsRoute::class,
            MyShopsRoute::class,
            EditProfileScreenRoute::class,
            AuthScreenRoute::class,
            ShopBottomSheetRoute::class,
            ShopDetailRoute::class,
            ShopReviews::class,
            CreateShop::class,
            UpdateShop::class,
            AddCategoryDialog::class,
            ShopFilterDialog::class,
        )

        fun getRouteClass(route: NavBackStackEntry): KClass<out Route>? =
            allRoutes
                .firstOrNull { route.destination.hasRoute(it) }
                .also { if (it == null) Timber.e("Unknown route: $route") }
    }
}