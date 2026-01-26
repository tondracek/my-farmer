package com.tondracek.myfarmer.ui.auth.registrationscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.google.signInWithGoogleCredentialManager
import com.tondracek.myfarmer.ui.auth.loginscreen.LoginRoute
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects
import kotlinx.coroutines.launch

@Composable
fun RegistrationEffects(
    viewmodel: RegistrationViewmodel,
    navController: NavController,
    appUiController: AppUiController
) {
    val context = LocalContext.current
    val serverClientId = stringResource(R.string.default_web_client_id)
    val scope = rememberCoroutineScope()

    val registeredSuccessfullyMessage =
        stringResource(R.string.registered_successfully_please_complete_your_profile)
    val loggedInSuccessfullyMessage = stringResource(R.string.login_successful)
    viewmodel.CollectEffects { effect ->
        when (effect) {
            RegistrationEffect.GoToLoginScreen ->
                navController.navigate(LoginRoute) {
                    popUpTo(RegistrationRoute) { inclusive = true }
                }

            RegistrationEffect.GoToProfileScreen ->
                navController.navigateToGraph(NavGraph.MainFlow.Profile, saveState = false)

            is RegistrationEffect.ShowError ->
                appUiController.showError(effect.error)

            RegistrationEffect.ShowRegisteredSuccessfully -> {
                appUiController.showSuccessMessage(registeredSuccessfullyMessage)
            }

            RegistrationEffect.RequestGoogleSignIn -> scope.launch {
                signInWithGoogleCredentialManager(context, serverClientId)
                    .withSuccess { viewmodel.onEvent(RegistrationEvent.OnGoogleTokenReceived(it)) }
                    .withFailure { appUiController.showError(it.error) }
            }

            RegistrationEffect.ShowLoggedInSuccessfully ->
                appUiController.showSuccessMessage(loggedInSuccessfullyMessage)
        }
    }
}
