package com.tondracek.myfarmer.ui.profilescreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.LogoutUC
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.domain.usecaseresult.withFailure
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val logoutUC: LogoutUC,
) : BaseViewModel<ProfileScreenEffect>() {

    private val loggedInUser: Flow<SystemUser> = getLoggedInUserUC()
        .withFailure { emitEffect(ProfileScreenEffect.GoToAuth) }
        .getOrElse(null)
        .filterNotNull()

    val state: StateFlow<ProfileScreenState> = loggedInUser.map { user ->
        val userUiState = UserUiState.fromSystemUser(user)
        ProfileScreenState.Success(user = userUiState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileScreenState.Loading,
    )

    private suspend fun logout() =
        logoutUC()
            .withFailure { emitEffect(ProfileScreenEffect.ShowError(it.error)) }
            .withSuccess { emitEffect(ProfileScreenEffect.GoToAuth) }
            .withSuccess { emitEffect(ProfileScreenEffect.ShowLogoutSuccessMessage) }

    fun onEvent(event: ProfileScreenEvent) = viewModelScope.launch {
        when (event) {
            ProfileScreenEvent.OnLogoutClick -> emitEffect(ProfileScreenEffect.RequestLogoutConfirmation)
            ProfileScreenEvent.OnEditProfileClick -> emitEffect(ProfileScreenEffect.GoToEditProfile)
            ProfileScreenEvent.OnLogoutConfirmed -> logout()
            is ProfileScreenEvent.OnShowErrorMessage ->
                emitEffect(ProfileScreenEffect.ShowErrorMessage(event.error))
        }
    }
}


sealed interface ProfileScreenEffect {

    /** actions **/
    data object RequestLogoutConfirmation : ProfileScreenEffect

    /** navigation **/
    data object GoToAuth : ProfileScreenEffect
    data object GoToEditProfile : ProfileScreenEffect

    /** messages **/
    data class ShowError(val error: DomainError) : ProfileScreenEffect
    data class ShowErrorMessage(val errorMessage: String) : ProfileScreenEffect
    data object ShowLogoutSuccessMessage : ProfileScreenEffect
}

sealed interface ProfileScreenEvent {

    /** button clicks **/
    data object OnLogoutClick : ProfileScreenEvent
    data object OnEditProfileClick : ProfileScreenEvent

    /** show errors **/
    data class OnShowErrorMessage(val error: String) : ProfileScreenEvent

    /** confirmations **/
    data object OnLogoutConfirmed : ProfileScreenEvent
}