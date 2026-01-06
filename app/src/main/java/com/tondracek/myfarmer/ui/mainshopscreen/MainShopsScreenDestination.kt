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
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewEvent
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewModel
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapView
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewEvent
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewModel

fun NavGraphBuilder.mainShopsScreenDestination(
    navController: NavController,
) = routeDestination<Route.MainShopsRoute>({
    applyTopBarPadding = false
}) { appUiController ->
    val mainShopsScreenViewModel: MainShopsScreenViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        mainShopsScreenViewModel.events.collect { event ->
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
        shopsListViewModel.events.collect { event ->
            when (event) {
                ShopsListViewEvent.OnBackClicked ->
                    navController.navigateUp()

                is ShopsListViewEvent.OpenShopDetail ->
                    navController.navigate(Route.ShopDetailRoute(event.shopId.toString()))

                is ShopsListViewEvent.ShowError ->
                    appUiController.showError(event.message)
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
        shopsMapViewModel.events.collect { event ->
            when (event) {
                is ShopsMapViewEvent.OpenShopDetail ->
                    navController.navigate(Route.ShopDetailRoute(event.shopId.toString()))

                is ShopsMapViewEvent.ShowError ->
                    appUiController.showError(event.message)
            }
        }
    }

    ShopsMapView(
        state = shopsMapViewState,
        onShopSelected = shopsMapViewModel::onShopSelected,
    )
}
