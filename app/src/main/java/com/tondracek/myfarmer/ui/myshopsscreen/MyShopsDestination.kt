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
import com.tondracek.myfarmer.ui.core.navigation.Route.ShopDetailRoute
import com.tondracek.myfarmer.ui.core.navigation.Route.UpdateShop
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects

fun NavGraphBuilder.myShopsScreenDestination(
    navController: NavController,
) = routeDestination<Route.MyShopsRoute> { appUiController ->

    val viewModel: MyShopsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    ScreenScaffold(
        title = stringResource(R.string.my_shops),
        showTopBar = true,
        applyContentPaddingInternally = false,
    ) {
        MyShopsScreen(
            state = state,
            onShopClick = viewModel::navigateToShopDetail,
            onUpdateShopClick = viewModel::navigateToUpdateShop,
            onDeleteShopClick = viewModel::deleteShop,
            onCreateShopClick = viewModel::navigateToCreateShop,
        )
    }

    val shopDeletionConfirmationMessage = stringResource(R.string.shop_deletion_confirmation)
    viewModel.CollectEffects {
        when (it) {
            is MyShopsEffect.OpenShopDetail ->
                navController.navigate(ShopDetailRoute(it.shopId.toString()))

            MyShopsEffect.OpenCreateShop -> navController.navigate(Route.CreateShop)

            is MyShopsEffect.OpenUpdateShop ->
                navController.navigate(UpdateShop(it.shopId.toString()))

            is MyShopsEffect.ShowError -> appUiController.showError(it.error)

            is MyShopsEffect.RequestShopDeletionConfirmation ->
                appUiController.raiseConfirmationDialog(
                    message = shopDeletionConfirmationMessage,
                    onConfirm = it.deleteShop,
                )
        }
    }
}