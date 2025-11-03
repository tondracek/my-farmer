package com.tondracek.myfarmer.ui.editprofilescreen

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.core.usecaseresult.UCResult

sealed interface EditProfileScreenState {
    data class Success(
        val name: String,
        val profilePicture: ImageResource,
        val contactInfo: ContactInfo,
    ) : EditProfileScreenState

    data object Loading : EditProfileScreenState

    data class Error(val result: UCResult.Failure) : EditProfileScreenState
}