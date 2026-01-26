package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route

@Composable
fun ShopsListViewSection(
    navController: NavController,
    appUiController: AppUiController,
) {
    val shopsListViewModel: ShopsListViewModel = hiltViewModel()
    val state by shopsListViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        shopsListViewModel.effects.collect { event ->
            when (event) {
                is ShopsListViewEffect.OpenShopDetail ->
                    navController.navigate(Route.ShopDetailRoute(event.shopId.toString()))

                is ShopsListViewEffect.ShowError ->
                    appUiController.showError(event.error)
            }
        }
    }

    ShopsListView(
        state = state,
        onNavigateToShopDetail = shopsListViewModel::openShopDetail,
        onRefreshClick = shopsListViewModel::refreshShopsList,
    )
}