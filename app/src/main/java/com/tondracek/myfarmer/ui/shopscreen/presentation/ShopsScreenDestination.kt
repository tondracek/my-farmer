package com.tondracek.myfarmer.ui.shopscreen.presentation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.shopslistview.presentation.ShopsListView
import com.tondracek.myfarmer.ui.shopslistview.presentation.ShopsListViewModel
import com.tondracek.myfarmer.ui.shopsmapview.presentation.ShopsMapView
import com.tondracek.myfarmer.ui.shopsmapview.presentation.ShopsMapViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ShopsScreenRoute

fun NavGraphBuilder.shopsScreenDestination() {
    composable<ShopsScreenRoute> {
        val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
        val shopsListViewModel: ShopsListViewModel = hiltViewModel()

        val shopsMapViewState by shopsMapViewModel.state.collectAsState()
        val shopsListViewState by shopsListViewModel.state.collectAsState()

        ShopsScreen(
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