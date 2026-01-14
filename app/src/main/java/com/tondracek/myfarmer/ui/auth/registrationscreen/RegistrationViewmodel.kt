package com.tondracek.myfarmer.ui.auth.registrationscreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.LoginWithGoogleUC
import com.tondracek.myfarmer.auth.domain.usecase.RegisterUserUC
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.ui.auth.registrationscreen.components.RegistrationInput
import com.tondracek.myfarmer.ui.auth.registrationscreen.components.RegistrationValidation
import com.tondracek.myfarmer.ui.auth.registrationscreen.components.validateInput
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
class RegistrationViewmodel @Inject constructor(
    private val registerUserUC: RegisterUserUC,
    private val loginWithGoogleUC: LoginWithGoogleUC,
) : BaseViewModel<RegistrationEffect>() {

    private val _registrationInProgress = MutableStateFlow(false)

    private val _input = MutableStateFlow(RegistrationInput.Empty)

    private val _validation: MutableStateFlow<RegistrationValidation> =
        MutableStateFlow(RegistrationValidation.Valid)

    val state: StateFlow<RegistrationState> = combine(
        _input,
        _validation,
        _registrationInProgress,
    ) { input, validation, registrationInProgress ->
        RegistrationState.Input(
            input = input,
            validation = validation,
            registrationInProgress = registrationInProgress,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RegistrationState.Loading,
    )

    private suspend fun submitRegistration() {
        val currentInput = _input.value

        val validation = validateInput(currentInput)
        if (!validation.isValid()) {
            _validation.emit(validation)
        }

        _registrationInProgress.emit(true)
        val result = registerUserUC(currentInput.email, currentInput.password)
        _registrationInProgress.emit(false)

        when (result) {
            is DomainResult.Success -> {
                emitEffect(RegistrationEffect.ShowRegisteredSuccessfully)
                emitEffect(RegistrationEffect.GoToProfileScreen)
            }

            is DomainResult.Failure -> emitEffect(RegistrationEffect.ShowError(result.error))
        }
    }

    private suspend fun submitGoogleLogin(token: String) {
        val result = loginWithGoogleUC(token)
        when (result) {
            is DomainResult.Success -> {
                emitEffect(RegistrationEffect.ShowLoggedInSuccessfully)
                emitEffect(RegistrationEffect.GoToProfileScreen)
            }

            is DomainResult.Failure -> emitEffect(RegistrationEffect.ShowError(result.error))
        }
    }

    fun onEvent(event: RegistrationEvent) = viewModelScope.launch {
        when (event) {
            /* INPUT EVENTS */
            is RegistrationEvent.EmailChanged -> {
                _input.update { it.copy(email = event.email) }
                _validation.update { it.copy(emailError = null) }
            }

            is RegistrationEvent.PasswordChanged -> {
                _input.update { it.copy(password = event.password) }
                _validation.update { it.copy(passwordError = null) }
            }

            is RegistrationEvent.ConfirmPasswordChanged -> {
                _input.update { it.copy(confirmPassword = event.confirmPassword) }
                _validation.update { it.copy(confirmPasswordError = null) }
            }

            /* BUTTON EVENTS */
            RegistrationEvent.GoToLoginScreenClicked ->
                emitEffect(RegistrationEffect.GoToLoginScreen)

            RegistrationEvent.RegisterButtonClicked -> submitRegistration()

            RegistrationEvent.GoogleSignInClicked -> {
                _registrationInProgress.emit(true)
                emitEffect(RegistrationEffect.LaunchGoogleSignIn)
            }

            is RegistrationEvent.GoogleTokenReceived ->
                submitGoogleLogin(event.idToken)
        }
    }
}

sealed interface RegistrationState {

    data class Input(
        val input: RegistrationInput,
        val validation: RegistrationValidation,
        val registrationInProgress: Boolean,
    ) : RegistrationState

    data object Loading : RegistrationState
}

sealed interface RegistrationEvent {
    /** Input events */
    data class EmailChanged(val email: String) : RegistrationEvent
    data class PasswordChanged(val password: String) : RegistrationEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationEvent

    /** Button events */
    data object RegisterButtonClicked : RegistrationEvent
    data object GoToLoginScreenClicked : RegistrationEvent

    /** Google sign-in */
    data object GoogleSignInClicked : RegistrationEvent
    data class GoogleTokenReceived(val idToken: String) : RegistrationEvent
}

sealed interface RegistrationEffect {

    data object GoToProfileScreen : RegistrationEffect

    data object GoToLoginScreen : RegistrationEffect


    data object LaunchGoogleSignIn : RegistrationEffect


    data class ShowError(val error: DomainError) : RegistrationEffect
    data object ShowRegisteredSuccessfully : RegistrationEffect
    data object ShowLoggedInSuccessfully : RegistrationEffect
}