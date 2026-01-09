package com.tondracek.myfarmer.ui.createshopflow.flowcreate

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategorySerializable
import com.tondracek.myfarmer.shopcategory.domain.model.toDomain
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.navigateForResult
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowEffect
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowScreen
import com.tondracek.myfarmer.ui.createshopflow.NEW_CATEGORY_DIALOG_VALUE

fun NavGraphBuilder.createShopDestination(
    navController: NavController,
) = routeDestination<Route.CreateShop> { appUiController ->
    val viewModel: CreateShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val shopSuccessfullyCreatedMessage = stringResource(R.string.shop_created_successfully)
    val shopSuccessfullyUpdatedMessage = stringResource(R.string.shop_updated_successfully)

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                CreateUpdateShopFlowEffect.NavigateBack -> navController.navigateUp()
                CreateUpdateShopFlowEffect.OpenAddCategoryDialog -> {
                    navController.navigateForResult<ShopCategorySerializable>(
                        route = Route.AddCategoryDialog,
                        key = NEW_CATEGORY_DIALOG_VALUE,
                        onResult = { newCategory ->
                            val category = newCategory.toDomain()
                            viewModel.addCategory(category)
                        }
                    )
                }

                is CreateUpdateShopFlowEffect.ShowError ->
                    appUiController.showErrorMessage(it.message)

                CreateUpdateShopFlowEffect.ShowShopCreatedSuccessfully ->
                    appUiController.showSuccessMessage(shopSuccessfullyCreatedMessage)

                CreateUpdateShopFlowEffect.ShowShopUpdatedSuccessfully ->
                    appUiController.showSuccessMessage(shopSuccessfullyUpdatedMessage)
            }
        }
    }


    ScreenScaffold(
        title = stringResource(R.string.create_shop_title)
    ) {
        CreateUpdateShopFlowScreen(
            state = state,
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
            onSubmitCreating = viewModel::submitCreating,
            onNavigateBack = viewModel::navigateBack
        )
    }
}
