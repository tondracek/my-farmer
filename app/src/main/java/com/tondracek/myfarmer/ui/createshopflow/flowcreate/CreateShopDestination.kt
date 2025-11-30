package com.tondracek.myfarmer.ui.createshopflow.flowcreate

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowMode
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowScreen

fun NavGraphBuilder.createShopDestination() = routeDestination<Route.CreateShop>(
    title = { stringResource(R.string.create_shop_title) },
) {
    val viewModel: CreateShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    CreateShopFlowScreen(
        state = state,
        createShopFlowMode = CreateShopFlowMode.CREATE,
        onNextStep = viewModel::goToNextStep,
        onPreviousStep = viewModel::goToPreviousStep,
        onUpdateName = viewModel::updateName,
        onUpdateDescription = viewModel::updateDescription,
        onUpdateCategories = viewModel::updateCategories,
        onUpdateImages = viewModel::updateImages,
        onUpdateLocation = viewModel::updateLocation,
        onUpdateOpeningHours = viewModel::updateOpeningHours,
        onUpdateMenu = viewModel::updateMenu,
        onSubmitCreating = viewModel::submitCreating,
        onNavigateBack = viewModel::navigateBack
    )
}


