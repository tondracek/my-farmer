package com.tondracek.myfarmer.review.domain.model

import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.user.domain.model.UserId

data class ReviewInput(
    val rating: Rating,
    val comment: String,
)

fun ReviewInput.toReview(
    shopId: ShopId,
    userId: UserId,
): Review = Review(
    id = ReviewId.newId(),
    shopId = shopId,
    userId = userId,
    rating = this.rating,
    comment = this.comment,
)