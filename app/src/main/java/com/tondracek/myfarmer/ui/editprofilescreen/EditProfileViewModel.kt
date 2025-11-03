package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getLoggedInUserUC: GetLoggedInUserUC,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _state: MutableStateFlow<EditProfileScreenState> =
        MutableStateFlow(EditProfileScreenState.Loading)

    val state: StateFlow<EditProfileScreenState> = _state

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

    private fun SystemUser.toUiState(): EditProfileScreenState.Success =
        EditProfileScreenState.Success(
            name = this.name,
            profilePicture = this.profilePicture,
            contactInfo = this.contactInfo,
        )

    private fun navigateBack() = appNavigator.navigateBack()
}
