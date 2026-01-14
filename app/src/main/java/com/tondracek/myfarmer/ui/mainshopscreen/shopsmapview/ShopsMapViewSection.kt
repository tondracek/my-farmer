package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route.ShopReviews
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetContent
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetEffect
import com.tondracek.myfarmer.ui.shopbottomsheet.ShopBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopsMapViewSection(
    navController: NavHostController,
    appUiController: AppUiController,
) {
    val mapViewModel: ShopsMapViewModel = hiltViewModel()
    val mapState by mapViewModel.state.collectAsState()

    val sheetViewModel: ShopBottomSheetViewModel = hiltViewModel()
    val sheetState by sheetViewModel.state.collectAsState()

    // Selected on the map
    val selectedShopId by mapViewModel.selectedShopId.collectAsState()
    // Actually opened in the sheet
    val openedShopId by sheetViewModel.openedShopId.collectAsState()

    ShopsMapView(
        navController = navController,
        state = mapState,
        onShopSelected = mapViewModel::onShopSelected,
    )

    mapViewModel.CollectEffects { effect ->
        when (effect) {
            is ShopsMapViewEffect.ShowError ->
                appUiController.showError(effect.error)

            is ShopsMapViewEffect.OpenShopDetail ->
                sheetViewModel.openShop(effect.shopId)
        }
    }

    AnimatedVisibility(openedShopId != null) {
        ModalBottomSheet(
            onDismissRequest = {
                sheetViewModel.closeShop()
                mapViewModel.onShopDeselected()
            },
            sheetState = rememberModalBottomSheetState(),
        ) {
            ShopBottomSheetContent(
                state = sheetState,
                navigateToReviews = sheetViewModel::navigateToReviews,
                showErrorMessage = appUiController::showErrorMessage,
            )
        }
    }

    sheetViewModel.CollectEffects { effect ->
        when (effect) {
            is ShopBottomSheetEffect.NavigateToReviews -> {
                navController.navigate(ShopReviews(shopId = effect.shopId.toString()))
                sheetViewModel.closeShop()
            }

            is ShopBottomSheetEffect.EmitError ->
                appUiController.showError(effect.error)
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(backStackEntry) {
        backStackEntry?.lifecycle?.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            sheetViewModel.openShop(selectedShopId)
        }
    }
}