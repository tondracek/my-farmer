package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.google.rememberGoogleSignInClient
import com.tondracek.myfarmer.ui.auth.common.google.rememberGoogleSignInLauncher
import com.tondracek.myfarmer.ui.auth.loginscreen.components.ForgotPasswordDialog
import com.tondracek.myfarmer.ui.auth.registrationscreen.RegistrationRoute
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
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

    var openForgotPasswordDialog by remember { mutableStateOf(false) }

    val googleLauncher = rememberGoogleSignInLauncher(
        onTokenReceived = { token ->
            viewmodel.onEvent(LoginEvent.GoogleTokenReceived(token))
        },
        onError = { message -> appUiController.showErrorMessage(message) }
    )
    val googleClient = rememberGoogleSignInClient()

    val loginSuccessfullyMessage =
        stringResource(R.string.login_successful)
    val sentPasswordResetEmailSuccessfullyMessage =
        stringResource(R.string.password_reset_mail_sent_to_your_address)
    viewmodel.CollectEffects { effect ->
        when (effect) {
            LoginEffect.GoToRegistrationScreen ->
                navController.navigate(RegistrationRoute) {
                    popUpTo(LoginRoute) { inclusive = true }
                }

            LoginEffect.GoToProfileScreen ->
                navController.navigateToGraph(NavGraph.MainFlow.Profile, saveState = false)

            is LoginEffect.ShowError ->
                appUiController.showError(effect.error)

            LoginEffect.ShowLoginSuccessfully ->
                appUiController.showSuccessMessage(loginSuccessfullyMessage)

            LoginEffect.ShowSentPasswordResetEmailSuccessfully ->
                appUiController.showSuccessMessage(sentPasswordResetEmailSuccessfullyMessage)

            LoginEffect.LaunchGoogleSignIn ->
                googleLauncher.launch(googleClient.signInIntent)

            LoginEffect.OpenForgotPasswordDialog ->
                openForgotPasswordDialog = true
        }
    }

    if (openForgotPasswordDialog)
        ForgotPasswordDialog(
            initialEmail = when (val currentState = state) {
                is LoginState.Input -> currentState.input.email
                else -> ""
            },
            onDismissRequest = { openForgotPasswordDialog = false },
            onSendEmailClicked = { email ->
                viewmodel.onEvent(LoginEvent.SendForgotPasswordEmailClicked(email))
                openForgotPasswordDialog = false
            }
        )
}