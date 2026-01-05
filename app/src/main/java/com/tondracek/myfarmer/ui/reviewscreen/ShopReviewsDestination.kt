package com.tondracek.myfarmer.ui.reviewscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import java.util.UUID

fun SavedStateHandle.getReviewsScreenShopId(): UUID =
    this.toRoute<Route.ShopReviews>()
        .let { UUID.fromString(it.shopId) }

fun NavGraphBuilder.shopReviewsScreenDestination() = routeDestination<Route.ShopReviews>({
    showTopBar = false
}) {
    val viewmodel: ShopReviewsViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    ShopReviewsScreen(
        state = state,
        onSubmitReview = viewmodel::onSubmitReview,
        onBackClick = viewmodel::onNavigateBack
    )
}