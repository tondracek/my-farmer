package com.tondracek.myfarmer.ui.shopfilterdialog

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.tondracek.myfarmer.ui.core.navigation.Route

fun SavedStateHandle.getShopFilterRepositoryKey(): String =
    toRoute<Route.ShopFilterDialog>().filterRepositoryKey

fun NavGraphBuilder.shopsFilterDialogDestination(
    navController: NavController,
) = dialog<Route.ShopFilterDialog> {
    val viewModel: ShopFilterDialogViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                ShopFilterDialogEffect.GoBack -> navController.navigateUp()
            }
        }
    }

    ShopFilterDialog(
        state = state,
        onCategoryFilterInputChange = viewModel::onCategoryFilterInputChange,

        onSelectedCategoriesAdd = viewModel::onSelectedCategoriesAdd,
        onSelectedCategoriesRemove = viewModel::onSelectedCategoriesRemove,

        onMaxDistanceChange = viewModel::onMaxDistanceChange,

        onMinRatingChange = viewModel::onMinRatingChange,

        onApplyFiltersClick = viewModel::onApplyFiltersClick,
        onCancelClick = viewModel::onCancelClick,
    )
}