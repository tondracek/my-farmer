package com.tondracek.myfarmer.ui.common.review

import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.time.Instant

data class ReviewUiState(
    val id: ReviewId,
    val author: SystemUser,
    val rating: Rating,
    val comment: String?,
    val createdAt: Instant?,
)

fun Review.toUiState(author: SystemUser): ReviewUiState = ReviewUiState(
    id = id,
    author = author,
    rating = rating,
    comment = comment,
    createdAt = createdAt,
)