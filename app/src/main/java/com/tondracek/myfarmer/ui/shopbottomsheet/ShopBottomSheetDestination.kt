package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.Route.ShopReviews
import com.tondracek.myfarmer.ui.shopdetailscreen.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetailscreen.components.ShopDetailLayout

const val SAVED_STATE_HANDLE_SHOP_BOTTOM_SHEET_SHOP_ID =
    "shop_bottom_sheet_shop_id"

fun SavedStateHandle.getShopBottomSheetShopId(): ShopId =
    ShopId.fromString(toRoute<Route.ShopBottomSheetRoute>().shopId)

@Composable
fun ShopBottomSheetDestination(
    navController: NavController,
    appUiController: AppUiController,
) {
    val viewmodel: ShopBottomSheetViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { event ->
            when (event) {
                is ShopBottomSheetEffect.NavigateToReviews ->
                    navController.navigate(ShopReviews(shopId = event.shopId.toString()))

                is ShopBottomSheetEffect.EmitError ->
                    appUiController.showError(event.error)
            }
        }
    }

    ShopBottomSheetContent(
        state = state,
        navigateToReviews = viewmodel::navigateToReviews,
        showErrorMessage = appUiController::showErrorMessage,
    )
}

@Composable
fun ShopBottomSheetContent(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> ShopDetailLayout(
            state = state,
            onReviewsClick = navigateToReviews,
            showErrorMessage = showErrorMessage,
        )

        ShopDetailState.Loading -> LoadingLayout()
    }
}