package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route

@Composable
fun ShopsListViewSection(
    navController: NavController,
    appUiController: AppUiController,
) {
    val shopsListViewModel: ShopsListViewModel = hiltViewModel()
    val shopsPaged = shopsListViewModel.shopsUiData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        shopsListViewModel.effects.collect { event ->
            when (event) {
                ShopsListViewEffect.OnBackClicked ->
                    navController.navigateUp()

                is ShopsListViewEffect.OpenShopDetail ->
                    navController.navigate(Route.ShopDetailRoute(event.shopId.toString()))

                is ShopsListViewEffect.ShowError ->
                    appUiController.showError(event.error)
            }
        }
    }

    ShopsListView(
        state = ShopsListViewState.Success(shopsPaged),
        onNavigateToShopDetail = shopsListViewModel::openShopDetail
    )
}