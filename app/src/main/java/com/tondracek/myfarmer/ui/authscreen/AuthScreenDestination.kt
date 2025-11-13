package com.tondracek.myfarmer.ui.authscreen

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.mainshopscreen.navigateToMainShopScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

fun AppNavigator.navigateToAuthScreen() =
    navigate(Route.AuthScreenRoute)

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

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
) : ViewModel() {

    fun navigateToMainShopsScreen() =
        appNavigator.navigateToMainShopScreen()

    fun showError(context: Context, message: String) =
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
}