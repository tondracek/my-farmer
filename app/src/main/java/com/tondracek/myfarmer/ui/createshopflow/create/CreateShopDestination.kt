package com.tondracek.myfarmer.ui.createshopflow.create

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowContent

fun NavGraphBuilder.createShopDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<Route.CreateShop> {
    val viewModel: CreateShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    ScreenScaffold(
        title = stringResource(R.string.create_shop_title)
    ) {
        ShopFlowContent(
            state = state,
            onShopFormEvent = viewModel::onShopFormEvent,
            onShopFlowEvent = viewModel::onShopFlowEvent,
        )
    }

    val shopSuccessfullyCreatedMessage = stringResource(R.string.shop_created_successfully)
    viewModel.CollectEffects {
        when (it) {
            CreateShopEffect.ShowCreatedSuccessfully ->
                appUiController.showSuccessMessage(shopSuccessfullyCreatedMessage)

            is CreateShopEffect.ShowError ->
                appUiController.showError(it.error)

            CreateShopEffect.ExitShopCreation ->
                navController.navigateUp()
        }
    }
}
