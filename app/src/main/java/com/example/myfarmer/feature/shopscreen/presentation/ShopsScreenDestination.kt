package com.example.myfarmer.feature.shopscreen.presentation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myfarmer.feature.shopscreen.presentation.listview.ShopsListViewModel
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapViewModel
import com.example.myfarmer.feature.shopscreen.presentation.root.ShopsScreen
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
            mapViewState = shopsMapViewState,
            onShopSelected = shopsMapViewModel::onShopSelected,
            listViewState = shopsListViewState,
            navigateToShopDetail = shopsListViewModel::navigateToShopDetail,
        )
    }
}