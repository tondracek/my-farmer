package com.tondracek.myfarmer.ui.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavGraph {

    @Serializable
    data object MainFlow : NavGraph {

        @Serializable
        data object MyShops : NavGraph

        @Serializable
        data object Home : NavGraph

        @Serializable
        data object Profile : NavGraph

        @Serializable
        data object Auth : NavGraph
    }
}

interface Route {

    @Serializable
    data object MainShopsRoute : Route

    @Serializable
    data object MyShopsRoute : Route

    @Serializable
    data object EditProfileScreenRoute : Route

    @Serializable
    data class ShopDetailRoute(val shopId: String) : Route

    @Serializable
    data class ShopReviews(val shopId: String) : Route

    @Serializable
    data object CreateShop : Route

    @Serializable
    data class UpdateShop(val shopId: String) : Route
}