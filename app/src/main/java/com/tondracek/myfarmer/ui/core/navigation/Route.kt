package com.tondracek.myfarmer.ui.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object MainShopsScreenRoute : Route

    @Serializable
    data object MyShopsRoute : Route

    @Serializable
    data object EditProfileScreenRoute : Route

    @Serializable
    data object AuthScreenRoute : Route
}