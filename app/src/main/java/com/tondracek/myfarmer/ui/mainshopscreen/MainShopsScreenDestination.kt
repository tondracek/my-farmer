package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.shopslistview.ShopsListView
import com.tondracek.myfarmer.ui.shopslistview.ShopsListViewModel
import com.tondracek.myfarmer.ui.shopsmapview.ShopsMapView
import com.tondracek.myfarmer.ui.shopsmapview.ShopsMapViewModel

fun AppNavigator.navigateToMainShopScreen() =
    navigate(Route.MainShopsScreenRoute)

fun NavGraphBuilder.mainShopsScreenDestination() {
    composable<Route.MainShopsScreenRoute> {
        val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
        val shopsMapViewState by shopsMapViewModel.state.collectAsState()

        val shopsListViewModel: ShopsListViewModel = hiltViewModel()
        val shopsListViewState by shopsListViewModel.state.collectAsState()

        MainShopsScreen(
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
                )
            }
        )
    }
}