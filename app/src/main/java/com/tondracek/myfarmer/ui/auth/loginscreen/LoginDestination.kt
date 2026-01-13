package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.google.rememberGoogleSignInClient
import com.tondracek.myfarmer.ui.auth.common.google.rememberGoogleSignInLauncher
import com.tondracek.myfarmer.ui.auth.registrationscreen.RegistrationRoute
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute : Route

fun NavGraphBuilder.loginDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<LoginRoute> {

    val viewmodel: LoginViewmodel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    LoginScreen(
        state = state,
        onEvent = viewmodel::onEvent,
    )

    val googleLauncher = rememberGoogleSignInLauncher(
        onTokenReceived = { token ->
            viewmodel.onEvent(LoginEvent.GoogleTokenReceived(token))
        },
        onError = { message -> appUiController.showErrorMessage(message) }
    )
    val googleClient = rememberGoogleSignInClient()

    val loginSuccessfullyMessage = stringResource(R.string.login_successful)
    viewmodel.CollectEffects { effect ->
        when (effect) {
            LoginEffect.GoToRegistrationScreen ->
                navController.navigate(RegistrationRoute)

            LoginEffect.GoToProfileScreen ->
                navController.navigate(Route.EditProfileScreenRoute)

            is LoginEffect.ShowError ->
                appUiController.showError(effect.error)

            LoginEffect.ShowLoginSuccessfully ->
                appUiController.showSuccessMessage(loginSuccessfullyMessage)

            LoginEffect.LaunchGoogleSignIn ->
                googleLauncher.launch(googleClient.signInIntent)
        }
    }
}
