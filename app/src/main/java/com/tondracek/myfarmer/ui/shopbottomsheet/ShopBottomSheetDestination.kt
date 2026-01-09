package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.stefanoq21.material3.navigation.bottomSheet
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.shopdetailscreen.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetailscreen.components.ShopDetailLayout
import java.util.UUID

fun SavedStateHandle.getShopBottomSheetShopId(): ShopId =
    UUID.fromString(toRoute<Route.ShopBottomSheetRoute>().shopId)

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.shopBottomSheetDestination(
    navController: NavController,
    appUiController: AppUiController,
) = bottomSheet<Route.ShopBottomSheetRoute> {
    val viewmodel: ShopBottomSheetViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { event ->
            when (event) {
                ShopBottomSheetEffect.NavigateBack ->
                    navController.navigateUp()

                is ShopBottomSheetEffect.NavigateToReviews ->
                    navController.navigate(Route.ShopReviews(shopId = event.shopId.toString()))
            }
        }
    }

    Content(
        state = state,
        navigateToReviews = viewmodel::navigateToReviews,
        onNavigateBack = viewmodel::navigateBack,
        showErrorMessage = appUiController::showErrorMessage,
    )
}

@Composable
private fun Content(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
    onNavigateBack: () -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> ShopDetailLayout(
            state = state,
            onReviewsClick = navigateToReviews,
            showErrorMessage = showErrorMessage,
        )

        ShopDetailState.Loading -> LoadingLayout()
        is ShopDetailState.Error -> ErrorLayout(
            failure = state.result,
            onNavigateBack = onNavigateBack
        )
    }
}