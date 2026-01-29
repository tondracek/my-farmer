package com.tondracek.myfarmer.ui.common.sample

import com.tondracek.myfarmer.review.sample.sampleReviews
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.common.review.toUiState
import com.tondracek.myfarmer.user.sample.sampleUsers

val sampleReviewsUI: List<ReviewUiState> = sampleReviews.mapNotNull { review ->
    val author = sampleUsers.find { it.id == review.userId } ?: return@mapNotNull null
    review.toUiState(author)
}