package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.auth.loginscreen.components.ForgotPasswordDialog
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.Route
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

    LoginEffects(
        viewmodel = viewmodel,
        navController = navController,
        appUiController = appUiController,
        onOpenForgotPasswordDialog = { openForgotPasswordDialog = true },
    )

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