package com.tondracek.myfarmer.ui.common.navbar

import com.tondracek.myfarmer.ui.core.navigation.Route

data class NavBarState(
    val currentRoute: Route? = null,
    val isLoggedIn: Boolean = false,
)