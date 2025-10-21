package com.tondracek.myfarmer.shopreview.domain.model

import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

typealias ShopReviewId = UUID

data class ShopReview(
    val user: SystemUser,
    val rating: Int,
    val comment: String?,
    val id: ShopReviewId = UUID.randomUUID(),
)
