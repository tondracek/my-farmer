package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun SavedStateHandle.getShopDetailScreenShopId(): ShopId =
    toRoute<Route.ShopDetailRoute>().shopId
        .let { ShopId.fromString(it) }

fun NavGraphBuilder.shopDetailScreenDestination() = routeDestination<Route.ShopDetailRoute>(
    title = { stringResource(R.string.shop_detail) }
) {
    val viewmodel: ShopDetailViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    ShopDetailScreen(
        state = state,
        navigateToReviews = viewmodel::navigateToReviews,
        onNavigateBack = viewmodel::navigateBack,
    )
}