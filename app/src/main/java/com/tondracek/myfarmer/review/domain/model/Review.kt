package com.tondracek.myfarmer.review.domain.model

import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import java.util.UUID

typealias ReviewId = UUID

data class Review(
    val id: ReviewId = UUID.randomUUID(),
    val shopId: ShopId,
    val userId: UserId,
    val rating: Rating,
    val comment: String?,
)

fun Collection<Review>.toAverageRating(): Rating =
    this.map { it.rating }.averageRating()