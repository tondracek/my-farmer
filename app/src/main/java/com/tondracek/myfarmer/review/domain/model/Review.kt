package com.tondracek.myfarmer.review.domain.model

import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import java.time.Instant

data class Review(
    val id: ReviewId = ReviewId.newId(),
    val shopId: ShopId,
    val userId: UserId,
    val rating: Rating,
    val comment: String?,
    val createdAt: Instant? = null,
)

fun Collection<Review>.toAverageRating(): Rating =
    this.map { it.rating }.averageRating()