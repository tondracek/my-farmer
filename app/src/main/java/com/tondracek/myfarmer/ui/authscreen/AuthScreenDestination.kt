package com.tondracek.myfarmer.ui.authscreen

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun NavGraphBuilder.authScreenDestination(
    navController: NavController,
) = routeDestination<Route.AuthScreenRoute> { appUiController ->

    val viewModel: AuthScreenViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthScreenEffect.NavigateToProfileScreen ->
                    navController.navigate(Route.EditProfileScreenRoute)

                is AuthScreenEffect.ShowError ->
                    appUiController.showError(effect.message)
            }
        }
    }

    FirebaseUiLoginScreen(
        onSuccess = viewModel::navigateToProfileScreen,
        onError = { message -> viewModel.showError(message) }
    )
}
