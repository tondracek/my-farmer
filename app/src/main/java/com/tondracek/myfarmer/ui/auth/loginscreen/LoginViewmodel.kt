package com.tondracek.myfarmer.ui.auth.loginscreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.LoginUserUC
import com.tondracek.myfarmer.auth.domain.usecase.LoginWithGoogleUC
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
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
) : BaseViewModel<LoginEffect>() {

    private val _loginInProgress = MutableStateFlow(false)

    private val _input = MutableStateFlow(LoginInput.Empty)

    private val _validation: MutableStateFlow<LoginValidation> =
        MutableStateFlow(LoginValidation.Valid)

    val state: StateFlow<LoginState> = combine(
        _input,
        _validation,
        _loginInProgress,
    ) { input, validation, loginInProgress ->
        LoginState.Input(
            input = input,
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
            is UCResult.Success -> {
                emitEffect(LoginEffect.ShowLoginSuccessfully)
                emitEffect(LoginEffect.GoToProfileScreen)
            }

            is UCResult.Failure -> emitEffect(LoginEffect.ShowError(result.userError))
        }
    }

    private suspend fun submitGoogleLogin(token: String) {
        val result = loginWithGoogleUC(token)
        when (result) {
            is UCResult.Success -> {
                emitEffect(LoginEffect.ShowLoginSuccessfully)
                emitEffect(LoginEffect.GoToProfileScreen)
            }

            is UCResult.Failure -> emitEffect(LoginEffect.ShowError(result.userError))
        }
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
            LoginEvent.GoToRegistrationScreenClicked ->
                emitEffect(LoginEffect.GoToRegistrationScreen)

            LoginEvent.LoginButtonClicked -> submitLogin()

            LoginEvent.GoogleSignInClicked -> emitEffect(LoginEffect.LaunchGoogleSignIn)

            /** Google Sign-In */
            is LoginEvent.GoogleTokenReceived -> submitGoogleLogin(event.token)
        }
    }
}

sealed interface LoginState {

    data class Input(
        val input: LoginInput,
        val validation: LoginValidation,
        val loginInProgress: Boolean,
    ) : LoginState

    data object Loading : LoginState
}

sealed interface LoginEvent {
    /** Input events */
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data class GoogleTokenReceived(val token: String) : LoginEvent

    /** Button events */
    data object LoginButtonClicked : LoginEvent

    /** Google Sign-In */
    data object GoogleSignInClicked : LoginEvent
    data object GoToRegistrationScreenClicked : LoginEvent
}

sealed interface LoginEffect {

    data object GoToProfileScreen : LoginEffect
    data object GoToRegistrationScreen : LoginEffect

    data object LaunchGoogleSignIn : LoginEffect

    data class ShowError(val message: String) : LoginEffect
    data object ShowLoginSuccessfully : LoginEffect
}