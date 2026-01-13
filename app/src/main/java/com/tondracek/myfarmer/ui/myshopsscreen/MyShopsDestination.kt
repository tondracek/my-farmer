package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects

fun NavGraphBuilder.myShopsScreenDestination(
    navController: NavController,
) = routeDestination<Route.MyShopsRoute> { appUiController ->

    val viewModel: MyShopsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    viewModel.CollectEffects {
        when (it) {
            MyShopsEffect.OnGoBack -> navController.navigateUp()

            is MyShopsEffect.OpenShopDetail ->
                navController.navigate(Route.ShopDetailRoute(it.shopId.toString()))

            MyShopsEffect.OpenCreateShop -> navController.navigate(Route.CreateShop)

            is MyShopsEffect.OpenUpdateShop ->
                navController.navigate(Route.UpdateShop(it.shopId.toString()))

            is MyShopsEffect.ShowError -> appUiController.showError(it.error)
        }
    }

    ScreenScaffold(
        title = stringResource(R.string.my_shops),
        showTopBar = true,
        applyTopBarPadding = false,
    ) {
        MyShopsScreen(
            state = state,
            onShopClick = viewModel::navigateToShopDetail,
            onUpdateShopClick = viewModel::navigateToUpdateShop,
            onDeleteShopClick = viewModel::deleteShop,
            onCreateShopClick = viewModel::navigateToCreateShop,
        )
    }
}