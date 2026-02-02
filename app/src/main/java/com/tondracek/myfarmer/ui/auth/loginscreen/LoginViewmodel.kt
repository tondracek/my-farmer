package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.LoginUserUC
import com.tondracek.myfarmer.auth.domain.usecase.LoginWithGoogleUC
import com.tondracek.myfarmer.auth.domain.usecase.SendForgotPasswordEmailUC
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.ui.auth.loginscreen.components.LoginInput
import com.tondracek.myfarmer.ui.auth.loginscreen.components.LoginValidation
import com.tondracek.myfarmer.ui.auth.loginscreen.components.validateInput
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val loginUserUC: LoginUserUC,
    private val loginWithGoogleUC: LoginWithGoogleUC,
    private val sendForgotPasswordEmail: SendForgotPasswordEmailUC,
) : BaseViewModel<LoginEffect>() {

    private val _loginInProgress = MutableStateFlow(false)

    private val _input = MutableStateFlow(LoginInput.Empty)

    private val _showForgotPassword = MutableStateFlow(false)

    private val _validation: MutableStateFlow<LoginValidation> =
        MutableStateFlow(LoginValidation.Valid)

    val state: StateFlow<LoginState> = combine(
        _input,
        _showForgotPassword,
        _validation,
        _loginInProgress,
    ) { input, showForgotPassword, validation, loginInProgress ->
        LoginState.Input(
            input = input,
            showForgotPassword = showForgotPassword,
            validation = validation,
            loginInProgress = loginInProgress,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginState.Loading,
    )

    private suspend fun submitLogin() {
        val currentInput = _input.value

        val validation = validateInput(currentInput)
        if (!validation.isValid())
            return _validation.emit(validation)

        _loginInProgress.emit(true)
        val result = loginUserUC(currentInput.email, currentInput.password)
        _loginInProgress.emit(false)

        when (result) {
            is DomainResult.Success -> {
                emitEffect(LoginEffect.ShowLoginSuccessfully)
                emitEffect(LoginEffect.GoToProfileScreen)
            }

            is DomainResult.Failure -> {
                if (result.error is AuthError.InvalidCredentials)
                    _showForgotPassword.emit(true)
                emitEffect(LoginEffect.ShowError(result.error))
            }
        }
    }

    private suspend fun submitGoogleLogin(token: String) =
        loginWithGoogleUC(token)
            .withFailure { emitEffect(LoginEffect.ShowError(it.error)) }
            .withSuccess {
                emitEffect(LoginEffect.ShowLoginSuccessfully)
                emitEffect(LoginEffect.GoToProfileScreen)
            }

    fun onEvent(event: LoginEvent) = viewModelScope.launch {
        when (event) {
            /** Input events */
            is LoginEvent.EmailChanged -> {
                _input.update { it.copy(email = event.email) }
                _validation.update { it.copy(emailError = null) }
            }

            is LoginEvent.PasswordChanged -> {
                _input.update { it.copy(password = event.password) }
                _validation.update { it.copy(passwordError = null) }
            }

            /** Button events */
            LoginEvent.LoginButtonClicked -> submitLogin()
            LoginEvent.GoToRegistrationScreenClicked ->
                emitEffect(LoginEffect.GoToRegistrationScreen)

            /** Forgot password */
            LoginEvent.ForgotPasswordClicked -> emitEffect(LoginEffect.OpenForgotPasswordDialog)
            is LoginEvent.SendForgotPasswordEmailClicked ->
                sendForgotPasswordEmail(event.email)
                    .withFailure { emitEffect(LoginEffect.ShowError(it.error)) }
                    .withSuccess { emitEffect(LoginEffect.ShowSentPasswordResetEmailSuccessfully) }

            /** Google Sign-In */
            LoginEvent.OnGoogleSignInClicked -> emitEffect(LoginEffect.RequestGoogleSignIn)
            is LoginEvent.OnGoogleTokenReceived -> submitGoogleLogin(event.token)
        }
    }
}

sealed interface LoginState {

    data class Input(
        val input: LoginInput,
        val showForgotPassword: Boolean,
        val validation: LoginValidation,
        val loginInProgress: Boolean,
    ) : LoginState

    data object Loading : LoginState
}

sealed interface LoginEvent {
    /** Input events */
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent

    /** Button events */
    data object LoginButtonClicked : LoginEvent
    data object GoToRegistrationScreenClicked : LoginEvent

    /** Google Sign-In */
    data object OnGoogleSignInClicked : LoginEvent
    data class OnGoogleTokenReceived(val token: String) : LoginEvent

    /** Forgot password */
    data object ForgotPasswordClicked : LoginEvent
    data class SendForgotPasswordEmailClicked(val email: String) : LoginEvent
}

sealed interface LoginEffect {

    /** Navigation */
    data object GoToProfileScreen : LoginEffect
    data object GoToRegistrationScreen : LoginEffect

    /** Messages */
    data class ShowError(val error: DomainError) : LoginEffect
    data object ShowLoginSuccessfully : LoginEffect
    data object ShowSentPasswordResetEmailSuccessfully : LoginEffect

    /** Dialogs */
    data object OpenForgotPasswordDialog : LoginEffect

    /** Google Sign-In */
    data object RequestGoogleSignIn : LoginEffect
}