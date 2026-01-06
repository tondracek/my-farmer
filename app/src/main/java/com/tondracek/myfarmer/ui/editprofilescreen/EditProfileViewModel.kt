package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.LogoutUC
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.usecase.UpdateUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val updateUserUC: UpdateUserUC,
    private val logout: LogoutUC,
) : ViewModel() {

    val loggedInUserUC: SharedFlow<UCResult<SystemUser>> = getLoggedInUserUC()
        .onEach { result ->
            if (result is UCResult.Failure) _effects.emit(EditProfileScreenEffect.OpenAuthScreen)
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1
        )

    private val _state: MutableStateFlow<EditProfileScreenState> =
        MutableStateFlow(EditProfileScreenState.Loading)

    val state: StateFlow<EditProfileScreenState> = combine(
        _state,
        loggedInUserUC,
    ) { state, loggedUserResult ->
        when (loggedUserResult) {
            is UCResult.Success -> state
            is UCResult.Failure -> EditProfileScreenState.Error(result = loggedUserResult)
                .also { _effects.emit(EditProfileScreenEffect.OpenAuthScreen) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditProfileScreenState.Loading
    )

    private val _effects = MutableSharedFlow<EditProfileScreenEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<EditProfileScreenEffect> = _effects

    init {
        viewModelScope.launch { loadData() }
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
        val updateUser = currentState.toSystemUser()

        _state.update { EditProfileScreenState.UpdatingProfile }
        val updateResult = updateUserUC(updateUser)

        when (updateResult) {
            is UCResult.Success -> run {
                _state.update { EditProfileScreenState.SavedSuccessfully }
                delay(3.seconds)
                loadData()
            }

            is UCResult.Failure -> _state.update { EditProfileScreenState.Error(result = updateResult) }
        }
    }

    fun navigateBack() = viewModelScope.launch {
        _effects.emit(EditProfileScreenEffect.GoBack)
    }

    /* PRIVATE HELPERS */

    private fun updateState(update: (EditProfileScreenState.Success) -> EditProfileScreenState) =
        _state.update {
            when (it) {
                is EditProfileScreenState.Success -> update(it)
                else -> it
            }
        }

    private suspend fun loadData() {
        val loggedUser = loggedInUserUC.first()

        _state.update { prevState: EditProfileScreenState ->
            loggedUser
                .mapSuccess { it.toUiState() }
                .getOrElse { EditProfileScreenState.Error(result = it) }
        }
    }
}

sealed interface EditProfileScreenEffect {
    data object GoBack : EditProfileScreenEffect

    data object OpenAuthScreen : EditProfileScreenEffect
}