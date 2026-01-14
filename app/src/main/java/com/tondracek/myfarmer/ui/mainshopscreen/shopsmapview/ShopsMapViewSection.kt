package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route.ShopReviews
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetContent
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetEffect
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopsMapViewSection(
    navController: NavHostController,
    appUiController: AppUiController,
) {
    val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
    val shopsMapViewState by shopsMapViewModel.state.collectAsState()

    val viewmodel: ShopBottomSheetViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    ShopsMapView(
        navController = navController,
        state = shopsMapViewState,
        onShopSelected = shopsMapViewModel::onShopSelected,
    )

    LaunchedEffect(Unit) {
        shopsMapViewModel.effects.collect { event ->
            when (event) {
                is ShopsMapViewEffect.OpenShopDetail ->
                    viewmodel.openShop(event.shopId)

                is ShopsMapViewEffect.ShowError ->
                    appUiController.showError(event.error)
            }
        }
    }

    val sheetState = rememberModalBottomSheetState()
    val openedShopId by viewmodel.openedShopId.collectAsState()
    if (openedShopId != null)
        ModalBottomSheet(
            onDismissRequest = { viewmodel.openShop(null) },
            sheetState = sheetState,
        ) {
            ShopBottomSheetContent(
                state = state,
                navigateToReviews = viewmodel::navigateToReviews,
                showErrorMessage = appUiController::showErrorMessage,
            )
        }

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
}
