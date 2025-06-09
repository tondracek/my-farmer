package com.example.myfarmer.feature.shopscreen.presentation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myfarmer.feature.shopscreen.presentation.listview.ShopsListViewModel
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapViewModel
import com.example.myfarmer.feature.shopscreen.presentation.root.ShopScreen
import com.example.myfarmer.feature.shopscreen.presentation.root.ShopScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ShopScreenRoute

fun NavGraphBuilder.shopScreenDestination() {
    composable<ShopScreenRoute> {
        val shopsScreenViewModel: ShopScreenViewModel = hiltViewModel()
        val shopsMapViewModel: ShopsMapViewModel = hiltViewModel()
        val shopsListViewModel: ShopsListViewModel = hiltViewModel()

        val shopsScreenState by shopsScreenViewModel.state.collectAsState()
        val shopsMapViewState by shopsMapViewModel.state.collectAsState()
        val shopsListViewState by shopsListViewModel.state.collectAsState()

        ShopScreen(
            shopsScreenState = shopsScreenState,
            onMapModeSelected = shopsScreenViewModel::onMapModeSelected,
            onListModeSelected = shopsScreenViewModel::onListModeSelected,
            mapState = shopsMapViewState,
            listViewState = shopsListViewState,
            onShopSelected = shopsMapViewModel::onShopSelected,
        )
    }
}