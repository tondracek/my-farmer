package com.tondracek.myfarmer.ui.createshopflow.update

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowContent

fun SavedStateHandle.getUpdateShopScreenShopId(): ShopId =
    toRoute<Route.UpdateShop>().shopId
        .let { ShopId.fromString(it) }

fun NavGraphBuilder.updateShopDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<Route.UpdateShop> {
    val viewModel: UpdateShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    ScreenScaffold(
        title = stringResource(R.string.update_shop_title)
    ) {
        ShopFlowContent(
            state = state,
            onShopFormEvent = viewModel::onShopFormEvent,
            onShopFlowEvent = viewModel::onShopFlowEvent,
        )
    }

    val shopSuccessfullyUpdatedMessage = stringResource(R.string.shop_updated_successfully)
    viewModel.CollectEffects {
        when (it) {
            UpdateShopEffect.ShowUpdatedSuccessfully ->
                appUiController.showSuccessMessage(shopSuccessfullyUpdatedMessage)

            is UpdateShopEffect.ShowError ->
                appUiController.showError(it.error)

            UpdateShopEffect.ExitShopUpdate ->
                navController.navigateUp()
        }
    }
}
