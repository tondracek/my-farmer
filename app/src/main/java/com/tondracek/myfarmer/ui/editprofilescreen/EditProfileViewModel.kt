package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.auth.domain.usecase.LogoutUC
import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.common.usecase.UpdateUC
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    isLoggedIn: IsLoggedInUC,
    private val updateUserUC: UpdateUC<SystemUser>,
    private val logout: LogoutUC,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _user: MutableStateFlow<SystemUser?> = MutableStateFlow(null)
    private val _state: MutableStateFlow<EditProfileScreenState> =
        MutableStateFlow(EditProfileScreenState.Loading)

    val state: StateFlow<EditProfileScreenState> = _state
        .onEach {
            if (!isLoggedIn.invokeSync()) appNavigator.navigate(Route.AuthScreenRoute)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditProfileScreenState.Loading
        )

    init {
        viewModelScope.launch {
            getLoggedInUserUC().collectLatest { result ->
                _state.update { prevState ->
                    result.map {
                        when (prevState) {
                            is EditProfileScreenState.Success -> prevState
                            else -> it.toUiState()
                        }
                    }.getOrElse { EditProfileScreenState.Error(result = it) }
                }
                _user.value = result.getOrNull()
            }
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
        val currentUser = _user.value ?: return@launch
        val currentState = _state.value as? EditProfileScreenState.Success ?: return@launch

        val updatedUser = currentState.toSystemUser(currentUser.id, currentUser.firebaseId)
        when (val result = updateUserUC(updatedUser)) {
            is UCResult.Success -> navigateBack()
            is UCResult.Failure -> _state.update { EditProfileScreenState.Error(result = result) }
        }
    }

    private fun SystemUser.toUiState(): EditProfileScreenState.Success =
        EditProfileScreenState.Success(
            name = this.name,
            profilePicture = this.profilePicture,
            contactInfo = this.contactInfo,
        )

    private fun EditProfileScreenState.Success.toSystemUser(
        id: UUID,
        firebaseId: String,
    ): SystemUser =
        SystemUser(
            id = id,
            firebaseId = firebaseId,
            name = this.name,
            profilePicture = this.profilePicture,
            contactInfo = this.contactInfo,
        )

    private fun updateState(
        update: (EditProfileScreenState.Success) -> EditProfileScreenState
    ) = _state.update {
        when (it) {
            is EditProfileScreenState.Success -> update(it)

            else -> EditProfileScreenState.Success(
                name = "",
                profilePicture = ImageResource.EMPTY,
                contactInfo = ContactInfo.EMPTY
            ).let { newSuccessState -> update(newSuccessState) }
        }
    }

    private fun navigateBack() = appNavigator.navigateBack()
}
