package com.tondracek.myfarmer.ui.reviewscreen

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.ui.common.review.ReviewUiState

sealed interface ShopReviewsScreenState {

    data class Success(
        val shopName: String?,
        val reviews: List<ReviewUiState>
    ) : ShopReviewsScreenState

    data object Loading : ShopReviewsScreenState

    data class Error(val failure: UCResult.Failure) : ShopReviewsScreenState
}
