package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.field.EmailInput
import com.tondracek.myfarmer.ui.auth.common.field.PasswordInput
import com.tondracek.myfarmer.ui.auth.common.google.GoogleSignInButton
import com.tondracek.myfarmer.ui.auth.loginscreen.components.LoginInput
import com.tondracek.myfarmer.ui.auth.loginscreen.components.LoginValidation
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    ScreenScaffold(
        title = stringResource(R.string.user_login),
    ) {
        when (state) {
            is LoginState.Input -> LoginInputScreen(
                state = state,
                onEvent = onEvent,
            )

            LoginState.Loading -> LoadingLayout()
        }
    }
}

@Composable
fun LoginInputScreen(
    state: LoginState.Input,
    onEvent: (LoginEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .widthIn(600.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        EmailInput(
            input = state.input.email,
            error = state.validation.emailError,
            onInputChanged = { onEvent(LoginEvent.EmailChanged(it)) },
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PasswordInput(
                input = state.input.password,
                error = state.validation.passwordError,
                onInputChanged = { onEvent(LoginEvent.PasswordChanged(it)) },
            )

            AnimatedVisibility(state.showForgotPassword) {
                TextButton(
                    onClick = { onEvent(LoginEvent.ForgotPasswordClicked) }
                ) {
                    Text(
                        text = "Forgot Password? Click here to reset it.",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }


        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.validation.isValid() && !state.loginInProgress,
            onClick = { onEvent(LoginEvent.LoginButtonClicked) }
        ) {
            when {
                state.loginInProgress -> CircularProgressIndicator()
                else -> Text(text = stringResource(R.string.login))
            }
        }

        GoogleSignInButton { onEvent(LoginEvent.OnGoogleSignInClicked) }

        TextButton(onClick = { onEvent(LoginEvent.GoToRegistrationScreenClicked) }) {
            Text(
                text = stringResource(R.string.don_t_have_an_account_register_by_clicking_here),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MyFarmerPreview {
        LoginScreen(
            state = LoginState.Input(
                input = LoginInput.Empty,
                showForgotPassword = true,
                validation = LoginValidation.Valid,
                loginInProgress = false,
            ),
            onEvent = {},
        )
    }
}