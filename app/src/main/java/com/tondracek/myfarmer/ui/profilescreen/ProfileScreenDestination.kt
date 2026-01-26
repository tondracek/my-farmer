package com.tondracek.myfarmer.ui.profilescreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object ProfileScreenRoute : Route

fun NavGraphBuilder.profileScreenDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<ProfileScreenRoute> {

    val viewModel: ProfileScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )

    ProfileEffects(
        navController = navController,
        appUiController = appUiController,
        viewModel = viewModel,
    )
}
