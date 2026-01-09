package com.tondracek.myfarmer.review.domain.model

import com.tondracek.myfarmer.systemuser.domain.model.UserId
import java.util.UUID

data class ReviewInput(
    val rating: Rating,
    val comment: String,
)

fun ReviewInput.toReview(
    shopId: UUID,
    userId: UserId,
): Review = Review(
    id = UUID.randomUUID(),
    shopId = shopId,
    userId = userId,
    rating = this.rating,
    comment = this.comment,
)