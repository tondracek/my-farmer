package com.tondracek.myfarmer.ui.authscreen

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.navigation.Route

fun NavGraphBuilder.authScreenDestination() {
    composable<Route.AuthScreenRoute> {
        val context = LocalContext.current

        val viewModel: AuthScreenViewModel = hiltViewModel()

        FirebaseUiLoginScreen(
            onSuccess = viewModel::navigateToMainShopsScreen,
            onError = { message -> viewModel.showError(context, message) }
        )
    }
}
