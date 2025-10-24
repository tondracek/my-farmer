package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.stefanoq21.material3.navigation.bottomSheet
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.shopdetaillayout.ShopDetailLayout
import com.tondracek.myfarmer.ui.shopdetaillayout.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetaillayout.ShopDetailViewModel
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ShopBottomSheetRoute(val shopId: String)

fun AppNavigator.navigateToShopBottomSheet(shopId: ShopId) =
    navigate(ShopBottomSheetRoute(shopId.toString()))

fun SavedStateHandle.getShopBottomSheetShopId(): ShopId =
    UUID.fromString(toRoute<ShopBottomSheetRoute>().shopId)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.shopBottomSheetDestination() {
    bottomSheet<ShopBottomSheetRoute> {
        val viewmodel: ShopDetailViewModel = hiltViewModel()
        val state by viewmodel.state.collectAsState()

        Content(
            state = state,
            navigateToReviews = viewmodel::navigateToReviews,
        )
    }
}

@Composable
private fun Content(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> ShopDetailLayout(
            state = state,
            onReviewsClick = navigateToReviews
        )

        ShopDetailState.Loading -> LoadingLayout()
        is ShopDetailState.Error -> Text(text = state.result.userError)
    }
}