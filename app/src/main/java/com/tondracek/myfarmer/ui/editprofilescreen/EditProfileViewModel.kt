package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state: MutableStateFlow<EditProfileScreenState> =
        MutableStateFlow(EditProfileScreenState.Loading)

    val state: StateFlow<EditProfileScreenState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditProfileScreenState.Loading
    )

    init {
        viewModelScope.launch {
            getLoggedInUserUC().collect {
                _state.value = when (it) {
                    is UCResult.Success -> it.data.toUiState()
                    is UCResult.Failure -> EditProfileScreenState.Error(result = it)
                }
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

    private fun SystemUser.toUiState(): EditProfileScreenState.Success =
        EditProfileScreenState.Success(
            name = this.name,
            profilePicture = this.profilePicture,
            contactInfo = this.contactInfo,
        )

    private fun updateState(
        update: (EditProfileScreenState.Success) -> EditProfileScreenState
    ) = _state.update {
        when (it) {
            is EditProfileScreenState.Success -> update(it)

            else -> update(
                EditProfileScreenState.Success(
                    name = "",
                    profilePicture = ImageResource.EMPTY,
                    contactInfo = ContactInfo.EMPTY
                )
            )
        }
    }

    private fun navigateBack() = appNavigator.navigateBack()
}
