package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.stefanoq21.material3.navigation.bottomSheet
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailState
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components.ShopDetailLayout
import com.tondracek.myfarmer.ui.core.navigation.Route
import java.util.UUID

fun SavedStateHandle.getShopBottomSheetShopId(): ShopId =
    UUID.fromString(toRoute<Route.ShopBottomSheetRoute>().shopId)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.shopBottomSheetDestination() {
    bottomSheet<Route.ShopBottomSheetRoute> { route ->
        val viewmodel: ShopBottomSheetViewModel = hiltViewModel()
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
        is ShopDetailState.Error -> ErrorLayout(text = state.result.userError)
    }
}