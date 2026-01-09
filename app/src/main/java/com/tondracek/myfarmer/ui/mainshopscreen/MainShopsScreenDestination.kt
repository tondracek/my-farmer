package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListView
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewEffect
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewModel
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapView
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewEvent
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewModel

fun NavGraphBuilder.mainShopsScreenDestination(
    navController: NavController,
) = routeDestination<Route.MainShopsRoute> { appUiController ->
    val mainShopsScreenViewModel: MainShopsScreenViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        mainShopsScreenViewModel.effects.collect { event ->
            when (event) {
                is MainShopsScreenEvent.OpenFiltersDialog ->
                    navController.navigate(Route.ShopFilterDialog(event.filtersKey))
            }
        }
    }

    MainShopsScreen(
        mapView = {
            ShopsMapViewSection(
                navController = navController,
                appUiController = appUiController,
            )
        },
        listView = {
            ShopsListViewSection(
                navController = navController,
                appUiController = appUiController,
            )
        },
        onOpenFiltersDialog = mainShopsScreenViewModel::onOpenFiltersDialog,
    )
}

@Composable
private fun ShopsListViewSection(
    navController: NavController,
    appUiController: AppUiController,
) {
    val shopsListViewModel: ShopsListViewModel = hiltViewModel()
    val shopsListViewState by shopsListViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        shopsListViewModel.effects.collect { event ->
            when (event) {
                ShopsListViewEffect.OnBackClicked ->
                    navController.navigateUp()

                is ShopsListViewEffect.OpenShopDetail ->
                    navController.navigate(Route.ShopDetailRoute(event.shopId.toString()))

                is ShopsListViewEffect.ShowError ->
                    appUiController.showErrorMessage(event.message)
            }
        }
    }

    ShopsListView(
        state = shopsListViewState,
        onNavigateToShopDetail = shopsListViewModel::openShopDetail,
        onNavigateBack = shopsListViewModel::onBackClicked
    )
}

@Composable
private fun ShopsMapViewSection(
    navController: NavController,
    appUiController: AppUiController,
) {
    val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
    val shopsMapViewState by shopsMapViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        shopsMapViewModel.effects.collect { event ->
            when (event) {
                is ShopsMapViewEvent.OpenShopDetail ->
                    navController.navigate(Route.ShopBottomSheetRoute(event.shopId.toString()))

                is ShopsMapViewEvent.ShowError ->
                    appUiController.showErrorMessage(event.message)
            }
        }
    }

    ShopsMapView(
        state = shopsMapViewState,
        onShopSelected = shopsMapViewModel::onShopSelected,
    )
}
