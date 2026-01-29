package com.tondracek.myfarmer.ui.editprofilescreen

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.model.UserId

sealed interface EditProfileScreenState {

    data class Success(
        val userInput: EditUserUiState,
        val wasChanged: Boolean,
    ) : EditProfileScreenState

    data object UpdatingProfile : EditProfileScreenState

    data object Loading : EditProfileScreenState
}

data class EditUserUiState(
    val name: String,
    val profilePicture: ImageResource,
    val contactInfo: ContactInfo,
) {
    companion object {
        val Empty = EditUserUiState(
            name = "",
            profilePicture = ImageResource.EMPTY,
            contactInfo = ContactInfo.EMPTY,
        )

        fun fromUser(user: SystemUser) = EditUserUiState(
            name = user.name,
            profilePicture = user.profilePicture,
            contactInfo = user.contactInfo,
        )
    }
}

fun EditUserUiState.applyEvent(event: EditProfileFormEvent): EditUserUiState = when (event) {
    is EditProfileFormEvent.OnNameChange -> this.copy(name = event.name)
    is EditProfileFormEvent.OnProfilePictureChange -> this.copy(profilePicture = event.profilePicture)
    is EditProfileFormEvent.OnContactInfoChange -> this.copy(contactInfo = event.contactInfo)
}

fun EditUserUiState.toSystemUser(id: UserId, authId: AuthId) = SystemUser(
    id = id,
    authId = authId,
    name = this.name,
    profilePicture = this.profilePicture,
    contactInfo = this.contactInfo,
)

fun EditUserUiState.isEqual(user: SystemUser): Boolean =
    this.name == user.name &&
            this.profilePicture == user.profilePicture &&
            this.contactInfo == user.contactInfo