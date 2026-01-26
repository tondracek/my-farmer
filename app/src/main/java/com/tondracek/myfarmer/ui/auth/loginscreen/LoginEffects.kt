package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.google.signInWithGoogleCredentialManager
import com.tondracek.myfarmer.ui.auth.registrationscreen.RegistrationRoute
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import kotlinx.coroutines.launch

@Composable
fun LoginEffects(
    viewmodel: LoginViewmodel,
    navController: NavController,
    appUiController: AppUiController,
    onOpenForgotPasswordDialog: () -> Unit,
) {
    val context = LocalContext.current
    val serverClientId = stringResource(R.string.default_web_client_id)
    val scope = rememberCoroutineScope()

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

            LoginEffect.RequestGoogleSignIn -> scope.launch {
                signInWithGoogleCredentialManager(context, serverClientId)
                    .withSuccess { viewmodel.onEvent(LoginEvent.OnGoogleTokenReceived(it)) }
                    .withFailure { appUiController.showError(it.error) }
            }

            LoginEffect.OpenForgotPasswordDialog -> onOpenForgotPasswordDialog()
        }
    }
}