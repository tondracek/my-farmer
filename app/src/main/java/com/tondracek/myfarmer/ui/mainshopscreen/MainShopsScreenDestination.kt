package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.ShopsListViewSection
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopsMapViewSection
import com.tondracek.myfarmer.ui.shopfilterdialog.ShopFilterDialog
import com.tondracek.myfarmer.ui.shopfilterdialog.ShopFilterDialogEffect
import com.tondracek.myfarmer.ui.shopfilterdialog.ShopFilterDialogViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.mainShopsScreenDestination(
    navController: NavHostController,
) = routeDestination<Route.MainShopsRoute> { appUiController ->
    val mainShopsScreenViewModel: MainShopsScreenViewModel = hiltViewModel()

    MainShopsScreen(
        mapView = {
            ShopsMapViewSection(
                navController = navController,
                appUiController = appUiController,
            )
        },
        listView = {
            ShopsListViewSection(
                navController = navController,
                appUiController = appUiController,
            )
        },
        onOpenFiltersDialog = mainShopsScreenViewModel::onOpenFiltersDialog,
    )

    var isFiltersDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainShopsScreenViewModel.effects.collect { event ->
            when (event) {
                is MainShopsScreenEvent.OpenFiltersDialog ->
                    isFiltersDialogOpen = true
            }
        }
    }

    val viewModel: ShopFilterDialogViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    if (isFiltersDialogOpen)
        BasicAlertDialog(onDismissRequest = {
            isFiltersDialogOpen = false
            viewModel.onCancelClick()
        }) {
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

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                ShopFilterDialogEffect.GoBack -> {
                    navController.navigateUp()
                    isFiltersDialogOpen = false
                }
            }
        }
    }
}
