package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.getOrElse
import com.tondracek.myfarmer.core.domain.domainresult.withFailure
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.user.domain.usecase.UpdateUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val updateUserUC: UpdateUserUC,
) : BaseViewModel<EditProfileScreenEffect>() {

    private val loggedInUser: StateFlow<SystemUser?> = getLoggedInUserUC()
        .withFailure { emitEffect(EditProfileScreenEffect.GoToAuth) }
        .getOrElse(null)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _input = MutableStateFlow(EditUserUiState.Empty)

    private val _isSaving = MutableStateFlow(false)

    val state: StateFlow<EditProfileScreenState> = combine(
        _isSaving,
        _input,
    ) { isSaving, input ->
        when (isSaving) {
            true -> EditProfileScreenState.UpdatingProfile
            false -> EditProfileScreenState.Success(userInput = input)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditProfileScreenState.Loading,
    )

    init {
        loadNewData()
    }

    private fun loadNewData() = viewModelScope.launch {
        loggedInUser
            .filterNotNull()
            .first()
            .let { user -> _input.update { EditUserUiState.fromUser(user) } }
    }

    private fun onSaveProfile() = viewModelScope.launch {
        val loggedUser = loggedInUser.value ?: return@launch
        val userInput = _input.value

        _isSaving.update { true }

        val updateUser = userInput.toSystemUser(id = loggedUser.id, authId = loggedUser.authId)
        updateUserUC(updateUser)
            .withFailure { emitEffect(EditProfileScreenEffect.ShowError(it.error)) }
            .withSuccess {
                emitEffect(EditProfileScreenEffect.NavigateBack)
                emitEffect(EditProfileScreenEffect.ShowSavedProfileMessage)
            }

        _isSaving.update { false }
    }

    private fun requestCancel() = viewModelScope.launch {
        val currentUser = loggedInUser.value ?: return@launch
        val currentInput = _input.value

        when (currentInput.isEqual(currentUser)) {
            true -> emitEffect(EditProfileScreenEffect.NavigateBack)
            false -> emitEffect(EditProfileScreenEffect.RequestCancelConfirmation)
        }
    }

    private fun confirmCancel() = viewModelScope.launch {
        emitEffect(EditProfileScreenEffect.NavigateBack)
    }

    fun onFormEvent(event: EditProfileFormEvent) =
        _input.update { it.applyEvent(event) }

    fun onScreenEvent(event: EditProfileScreenEvent) = when (event) {
        EditProfileScreenEvent.OnSaveClicked -> onSaveProfile()
        EditProfileScreenEvent.OnCancelClicked -> requestCancel()
        EditProfileScreenEvent.OnCancelConfirmed -> confirmCancel()
    }
}

sealed interface EditProfileScreenEffect {

    /** navigation **/
    data object GoToAuth : EditProfileScreenEffect
    data object NavigateBack : EditProfileScreenEffect

    /** messages **/
    data object ShowSavedProfileMessage : EditProfileScreenEffect
    data class ShowError(val error: DomainError) : EditProfileScreenEffect

    /** actions **/
    data object RequestCancelConfirmation : EditProfileScreenEffect
}

sealed interface EditProfileFormEvent {
    data class OnNameChange(val name: String) : EditProfileFormEvent
    data class OnProfilePictureChange(val profilePicture: ImageResource) : EditProfileFormEvent
    data class OnContactInfoChange(val contactInfo: ContactInfo) : EditProfileFormEvent
}

sealed interface EditProfileScreenEvent {

    /** button clicks **/
    data object OnSaveClicked : EditProfileScreenEvent
    data object OnCancelClicked : EditProfileScreenEvent

    /** confirmations **/
    data object OnCancelConfirmed : EditProfileScreenEvent
}