package com.tondracek.myfarmer.systemuser.domain.model

import com.tondracek.myfarmer.common.model.ImageResource
import java.util.UUID

typealias UserId = UUID

data class SystemUser(
    val id: UserId,
    val name: String,
    val profilePicture: ImageResource,
)
