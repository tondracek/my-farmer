package com.tondracek.myfarmer.ui.auth.registrationscreen

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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.field.EmailInput
import com.tondracek.myfarmer.ui.auth.common.field.PasswordInput
import com.tondracek.myfarmer.ui.auth.common.google.GoogleSignInButton
import com.tondracek.myfarmer.ui.auth.registrationscreen.components.RegistrationInput
import com.tondracek.myfarmer.ui.auth.registrationscreen.components.RegistrationValidation
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onEvent: (RegistrationEvent) -> Unit,
) {
    ScreenScaffold(
        title = stringResource(R.string.user_registration),
    ) {
        when (state) {
            is RegistrationState.Input -> RegistrationInputScreen(
                state = state,
                onEvent = onEvent,
            )

            RegistrationState.Loading -> LoadingLayout()
        }
    }
}

@Composable
fun RegistrationInputScreen(
    state: RegistrationState.Input,
    onEvent: (RegistrationEvent) -> Unit,
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
            onInputChanged = { onEvent(RegistrationEvent.EmailChanged(it)) },
        )

        PasswordInput(
            input = state.input.password,
            error = state.validation.passwordError,
            onInputChanged = { onEvent(RegistrationEvent.PasswordChanged(it)) },
            contentType = ContentType.NewPassword
        )

        PasswordInput(
            input = state.input.confirmPassword,
            error = state.validation.confirmPasswordError,
            onInputChanged = { onEvent(RegistrationEvent.ConfirmPasswordChanged(it)) },
            label = stringResource(R.string.confirm_password),
            contentType = ContentType.NewPassword
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.validation.isValid() && !state.registrationInProgress,
            onClick = { onEvent(RegistrationEvent.RegisterButtonClicked) }
        ) {
            when {
                state.registrationInProgress -> CircularProgressIndicator()
                else -> Text(text = stringResource(R.string.register))
            }
        }

        GoogleSignInButton { onEvent(RegistrationEvent.OnGoogleSignInClicked) }

        TextButton(onClick = { onEvent(RegistrationEvent.GoToLoginScreenClicked) }) {
            Text(
                text = stringResource(R.string.already_have_an_account_login_by_clicking_here),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    MyFarmerPreview {
        RegistrationScreen(
            state = RegistrationState.Input(
                input = RegistrationInput.Empty,
                validation = RegistrationValidation.Valid,
                registrationInProgress = false,
            ),
            onEvent = {},
        )
    }
}