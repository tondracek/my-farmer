package com.tondracek.myfarmer.ui.auth.registrationscreen

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
import com.tondracek.myfarmer.ui.auth.loginscreen.LoginRoute
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationRoute : Route

fun NavGraphBuilder.registrationDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<RegistrationRoute> {

    val viewmodel: RegistrationViewmodel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    RegistrationScreen(
        state = state,
        onEvent = viewmodel::onEvent,
    )

    val googleClient = rememberGoogleSignInClient()

    val googleLauncher = rememberGoogleSignInLauncher(
        onTokenReceived = { token ->
            viewmodel.onEvent(RegistrationEvent.GoogleTokenReceived(token))
        },
        onError = { message -> appUiController.showErrorMessage(message) }
    )

    val registeredSuccessfullyMessage =
        stringResource(R.string.registered_successfully_please_complete_your_profile)
    val loggedInSuccessfullyMessage = stringResource(R.string.login_successful)
    viewmodel.CollectEffects { effect ->
        when (effect) {
            RegistrationEffect.GoToLoginScreen ->
                navController.navigate(LoginRoute)

            RegistrationEffect.GoToProfileScreen ->
                navController.navigate(Route.EditProfileScreenRoute)

            is RegistrationEffect.ShowError ->
                appUiController.showError(effect.error)

            RegistrationEffect.ShowRegisteredSuccessfully -> {
                appUiController.showSuccessMessage(registeredSuccessfullyMessage)
            }

            RegistrationEffect.LaunchGoogleSignIn ->
                googleLauncher.launch(googleClient.signInIntent)

            RegistrationEffect.ShowLoggedInSuccessfully ->
                appUiController.showSuccessMessage(loggedInSuccessfullyMessage)
        }
    }
}
