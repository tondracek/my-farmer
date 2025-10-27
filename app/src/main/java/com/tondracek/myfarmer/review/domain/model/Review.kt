package com.tondracek.myfarmer.review.domain.model

import com.tondracek.myfarmer.systemuser.domain.model.UserId
import java.util.UUID

typealias ShopReviewId = UUID

data class Review(
    val id: ShopReviewId = UUID.randomUUID(),
    val userId: UserId,
    val rating: Int,
    val comment: String?,
)
