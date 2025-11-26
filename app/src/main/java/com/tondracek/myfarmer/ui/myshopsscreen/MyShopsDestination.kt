package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun NavGraphBuilder.myShopsScreenDestination() = routeDestination<Route.MyShopsRoute>(
    titleId = R.string.my_shops
) {

}