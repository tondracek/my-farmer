package com.tondracek.myfarmer.systemuser.domain.model

import com.tondracek.myfarmer.common.model.ImageResource
import java.util.UUID

typealias UserId = UUID

data class SystemUser(
    val id: UserId,
    val name: String,
    val profilePicture: ImageResource,
    val contactInfo: ContactInfo,
)

data class ContactInfo(
    val phoneNumber: String?,
    val email: String?,
    val website: MediaLink?,
    val facebook: MediaLink?,
    val instagram: MediaLink?,
)

data class MediaLink(
    val title: String,
    val url: String,
)
