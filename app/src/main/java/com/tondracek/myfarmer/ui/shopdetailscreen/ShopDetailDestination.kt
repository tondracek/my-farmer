package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.core.navigation.Route

fun SavedStateHandle.getShopDetailScreenShopId(): ShopId =
    toRoute<Route.ShopDetailRoute>().shopId
        .let { ShopId.fromString(it) }

fun NavGraphBuilder.shopDetailScreenDestination() {
    composable<Route.ShopDetailRoute> {
        val viewmodel: ShopDetailViewModel = hiltViewModel()
        val state by viewmodel.state.collectAsState()

        ShopDetailScreen(
            state = state,
            navigateToReviews = viewmodel::navigateToReviews,
        )
    }
}