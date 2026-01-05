package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun NavGraphBuilder.myShopsScreenDestination() = routeDestination<Route.MyShopsRoute>({
    title = stringResource(R.string.my_shops)
    applyTopBarPadding = false
}) {
    val viewModel: MyShopsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    MyShopsScreen(
        state = state,
        onShopClick = viewModel::navigateToShopDetail,
        onUpdateShopClick = viewModel::navigateToUpdateShop,
        onDeleteShopClick = viewModel::deleteShop,
        onCreateShopClick = viewModel::navigateToCreateShop,
        onNavigateBack = viewModel::navigateBack,
    )
}