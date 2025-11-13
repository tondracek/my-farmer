package com.tondracek.myfarmer.systemuser.domain.model

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import java.util.UUID

typealias UserId = UUID

data class SystemUser(
    val id: UserId,
    val firebaseId: String,
    val name: String,
    val profilePicture: ImageResource,
    val contactInfo: ContactInfo,
) {
    companion object {
        fun createEmpty(firebaseId: String) = SystemUser(
            id = UUID.randomUUID(),
            firebaseId = firebaseId,
            name = "",
            profilePicture = ImageResource.EMPTY,
            contactInfo = ContactInfo.EMPTY,
        )
    }
}