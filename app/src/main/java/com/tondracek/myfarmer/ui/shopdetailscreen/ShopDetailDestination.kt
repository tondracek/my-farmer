package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun SavedStateHandle.getShopDetailScreenShopId(): ShopId =
    toRoute<Route.ShopDetailRoute>().shopId
        .let { ShopId.fromString(it) }

fun NavGraphBuilder.shopDetailScreenDestination(
    navController: NavController,
) = routeDestination<Route.ShopDetailRoute> { appUiController ->
    val viewmodel: ShopDetailViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { event ->
            when (event) {
                ShopDetailEffect.NavigateBack ->
                    navController.navigateUp()

                is ShopDetailEffect.NavigateToReviews ->
                    navController.navigate(Route.ShopReviews(shopId = event.shopId.toString()))
            }
        }
    }

    ScreenScaffold {
        ShopDetailScreen(
            state = state,
            navigateToReviews = viewmodel::navigateToReviews,
            onNavigateBack = viewmodel::navigateBack,
            showErrorMessage = appUiController::showErrorMessage,
        )
    }
}