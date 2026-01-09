package com.tondracek.myfarmer.ui.common.review

import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser

data class ReviewUiState(
    val author: SystemUser,
    val rating: Rating,
    val comment: String?,
)

fun Review.toUiState(author: SystemUser): ReviewUiState = ReviewUiState(
    author = author,
    rating = rating,
    comment = comment,
)