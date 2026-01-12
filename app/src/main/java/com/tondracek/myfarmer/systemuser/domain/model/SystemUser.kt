package com.tondracek.myfarmer.systemuser.domain.model

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo


data class SystemUser(
    val id: UserId,
    val authId: AuthId,
    val name: String,
    val profilePicture: ImageResource,
    val contactInfo: ContactInfo,
) {
    companion object {
        fun createEmpty(authId: AuthId) = SystemUser(
            id = UserId.newId(),
            authId = authId,
            name = "",
            profilePicture = ImageResource.EMPTY,
            contactInfo = ContactInfo.EMPTY,
        )
    }
}