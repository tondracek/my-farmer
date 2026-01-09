package com.tondracek.myfarmer.ui.editprofilescreen

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId

sealed interface EditProfileScreenState {
    data class Success(
        val name: String,
        val profilePicture: ImageResource,
        val contactInfo: ContactInfo,
    ) : EditProfileScreenState

    data object UpdatingProfile : EditProfileScreenState

    data object Loading : EditProfileScreenState

    companion object {
        val Empty = Success(
            name = "",
            profilePicture = ImageResource.EMPTY,
            contactInfo = ContactInfo.EMPTY,
        )
    }
}

fun SystemUser.toUiState(): EditProfileScreenState.Success =
    EditProfileScreenState.Success(
        name = this.name,
        profilePicture = this.profilePicture,
        contactInfo = this.contactInfo,
    )

fun EditProfileScreenState.Success.toSystemUser(
    id: UserId,
    authId: AuthId,
) = SystemUser(
    id = id,
    authId = authId,
    name = this.name,
    profilePicture = this.profilePicture,
    contactInfo = this.contactInfo,
)