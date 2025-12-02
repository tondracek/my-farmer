package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListView
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewModel
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapView
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewModel

fun NavGraphBuilder.mainShopsScreenDestination() = routeDestination<Route.MainShopsRoute>(
    applyTopBarPadding = false,
) {
    val mainShopsScreenViewModel: MainShopsScreenViewModel = hiltViewModel()
    val mainShopsScreenViewState by mainShopsScreenViewModel.state.collectAsState()

    val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
    val shopsMapViewState by shopsMapViewModel.state.collectAsState()

    val shopsListViewModel: ShopsListViewModel = hiltViewModel()
    val shopsListViewState by shopsListViewModel.state.collectAsState()

    MainShopsScreen(
        state = mainShopsScreenViewState,
        mapView = { modifier ->
            ShopsMapView(
                modifier = modifier,
                state = shopsMapViewState,
                onShopSelected = shopsMapViewModel::onShopSelected,
            )
        },
        listView = { modifier ->
            ShopsListView(
                modifier = modifier,
                state = shopsListViewState,
                onNavigateToShopDetail = shopsListViewModel::navigateToShopDetail,
                onNavigateBack = shopsListViewModel::navigateBack
            )
        },
        onOpenFiltersDialog = mainShopsScreenViewModel::onOpenFiltersDialog,
    )
}
