package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.LogoutUC
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.usecase.UpdateUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val updateUserUC: UpdateUserUC,
    private val logout: LogoutUC,
) : ViewModel() {

    private val loggedInUserFlow: SharedFlow<UCResult<SystemUser>> = getLoggedInUserUC()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1
        )

    private val loggedInUser: StateFlow<SystemUser?> = loggedInUserFlow
        .getOrElse(defaultValue = null) {
            viewModelScope.launch {
                emitError(it)
                _effects.emit(EditProfileScreenEffect.OpenAuthScreen)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val _state: MutableStateFlow<EditProfileScreenState> =
        MutableStateFlow(EditProfileScreenState.Loading)

    val state: StateFlow<EditProfileScreenState> = _state

    private val _effects = MutableSharedFlow<EditProfileScreenEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<EditProfileScreenEffect> = _effects

    private suspend fun emitError(message: String) =
        _effects.emit(EditProfileScreenEffect.ShowError(message))

    private suspend fun emitError(result: UCResult.Failure) =
        emitError(result.userError)

    init {
        viewModelScope.launch {
            loadUserData()
        }
    }

    fun onNameChange(newName: String) = updateState {
        it.copy(name = newName)
    }

    fun onProfilePictureChange(newProfilePicture: ImageResource) = updateState {
        it.copy(profilePicture = newProfilePicture)
    }

    fun onContactInfoChange(newContactInfo: ContactInfo) = updateState {
        it.copy(contactInfo = newContactInfo)
    }

    fun onLogout() = logout()

    fun onSaveProfile() = viewModelScope.launch {
        val currentState = _state.value as? EditProfileScreenState.Success ?: return@launch
        val loggedUser = loggedInUser.value ?: return@launch emitError(NotLoggedInUCResult())

        _state.update { EditProfileScreenState.UpdatingProfile }

        val updateUser = currentState.toSystemUser(id = loggedUser.id, authId = loggedUser.authId)

        when (val updateResult = updateUserUC(updateUser)) {
            is UCResult.Success -> {
                _effects.emit(EditProfileScreenEffect.ShowSavedProfileMessage)
                loadUserData()
            }

            is UCResult.Failure -> {
                emitError(updateResult.userError)
                _state.update { currentState }
            }
        }
    }

    /* PRIVATE HELPERS */

    private fun updateState(update: (EditProfileScreenState.Success) -> EditProfileScreenState) =
        _state.update {
            when (it) {
                is EditProfileScreenState.Success -> update(it)
                else -> it
            }
        }

    private suspend fun loadUserData() = loggedInUserFlow.first().getOrNull().let { user ->
        _state.update {
            user?.toUiState() ?: EditProfileScreenState.Empty
        }
    }
}

sealed interface EditProfileScreenEffect {
    data object GoBack : EditProfileScreenEffect

    data object OpenAuthScreen : EditProfileScreenEffect

    data object ShowSavedProfileMessage : EditProfileScreenEffect

    data class ShowError(val message: String) : EditProfileScreenEffect
}