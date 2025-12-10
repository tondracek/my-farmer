package com.tondracek.myfarmer.ui.authscreen

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun NavGraphBuilder.authScreenDestination() = routeDestination<Route.AuthScreenRoute> {
    val context = LocalContext.current

    val viewModel: AuthScreenViewModel = hiltViewModel()

    FirebaseUiLoginScreen(
        onSuccess = viewModel::navigateToProfileScreen,
        onError = { message -> viewModel.showError(context, message) }
    )
}
