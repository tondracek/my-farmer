package com.tondracek.myfarmer.ui.editprofilescreen

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

sealed interface EditProfileScreenState {
    data class Success(
        val id: UUID,
        val firebaseId: String,
        val name: String,
        val profilePicture: ImageResource,
        val contactInfo: ContactInfo,
    ) : EditProfileScreenState

    data object SavedSuccessfully : EditProfileScreenState

    data object Loading : EditProfileScreenState

    data class Error(val result: UCResult.Failure) : EditProfileScreenState
}

fun SystemUser.toUiState(): EditProfileScreenState.Success =
    EditProfileScreenState.Success(
        id = this.id,
        firebaseId = this.firebaseId,
        name = this.name,
        profilePicture = this.profilePicture,
        contactInfo = this.contactInfo,
    )

fun EditProfileScreenState.Success.toSystemUser() =
    SystemUser(
        id = id,
        firebaseId = firebaseId,
        name = this.name,
        profilePicture = this.profilePicture,
        contactInfo = this.contactInfo,
    )