package com.tondracek.myfarmer.ui.createshopflow.flowupdate

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowMode
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowScreen
import java.util.UUID

fun SavedStateHandle.getUpdateShopScreenShopId(): ShopId =
    toRoute<Route.UpdateShop>().shopId
        .let { UUID.fromString(it) }

fun NavGraphBuilder.updateShopDestination() = routeDestination<Route.UpdateShop>(
    title = { stringResource(R.string.update_shop_title) },
) {
    val viewModel: UpdateShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    CreateShopFlowScreen(
        state = state,
        createShopFlowMode = CreateShopFlowMode.UPDATE,
        onNextStep = viewModel::goToNextStep,
        onPreviousStep = viewModel::goToPreviousStep,
        onUpdateName = viewModel::updateName,
        onUpdateDescription = viewModel::updateDescription,
        onOpenAddCategoryDialog = viewModel::onOpenAddCategoryDialog,
        onUpdateCategories = viewModel::updateCategories,
        onUpdateImages = viewModel::updateImages,
        onUpdateLocation = viewModel::updateLocation,
        onUpdateOpeningHours = viewModel::updateOpeningHours,
        onUpdateMenu = viewModel::updateMenu,
        onSubmitCreating = viewModel::submitUpdating,
        onNavigateBack = viewModel::navigateBack
    )
}
