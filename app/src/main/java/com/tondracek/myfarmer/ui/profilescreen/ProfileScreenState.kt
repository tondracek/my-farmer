package com.tondracek.myfarmer.ui.profilescreen

import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.user.domain.model.SystemUser

sealed interface ProfileScreenState {

    data class Success(
        val user: UserUiState,
    ) : ProfileScreenState

    data object Loading : ProfileScreenState
}


data class UserUiState(
    val name: String,
    val profilePicture: ImageResource,
    val contactInfo: ContactInfo,
) {
    companion object {
        fun fromSystemUser(user: SystemUser): UserUiState = UserUiState(
            name = user.name,
            profilePicture = user.profilePicture,
            contactInfo = user.contactInfo,
        )
    }
}