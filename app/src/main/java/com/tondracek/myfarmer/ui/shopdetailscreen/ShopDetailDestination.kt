package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailViewModel
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import kotlinx.serialization.Serializable

@Serializable
data class ShopDetailDestination(val shopId: String)

fun AppNavigator.navigateToShopDetailScreen(shopId: ShopId) =
    navigate(ShopDetailDestination(shopId.toString()))

fun SavedStateHandle.getShopDetailScreenShopId(): ShopId =
    toRoute<ShopDetailDestination>().shopId
        .let { ShopId.fromString(it) }

fun NavGraphBuilder.shopDetailScreenDestination() {
    composable<ShopDetailDestination> {
        val viewmodel: ShopDetailViewModel = hiltViewModel()
        val state by viewmodel.state.collectAsState()

        ShopDetailScreen(
            state = state,
            navigateToReviews = viewmodel::navigateToReviews,
        )
    }
}