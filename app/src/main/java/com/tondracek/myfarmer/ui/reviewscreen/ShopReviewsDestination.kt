package com.tondracek.myfarmer.ui.reviewscreen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun SavedStateHandle.getReviewsScreenShopId(): ShopId =
    this.toRoute<Route.ShopReviews>()
        .let { ShopId.fromString(it.shopId) }

fun NavGraphBuilder.shopReviewsScreenDestination(
    navController: NavController,
) = routeDestination<Route.ShopReviews> { appUiController ->
    val viewmodel: ShopReviewsViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { event ->
            when (event) {
                is ShopReviewsEffect.ShowError -> appUiController.showError(event.error)
            }
        }
    }

    ShopReviewsScreen(
        state = state,
        onSubmitReview = viewmodel::onSubmitReview,
        onReviewDeleteClick = viewmodel::onReviewDeleteClick,
    )
}