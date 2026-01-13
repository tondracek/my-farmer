package com.tondracek.myfarmer.ui.reviewscreen

import androidx.paging.PagingData
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import kotlinx.coroutines.flow.Flow

sealed interface ShopReviewsScreenState {

    data class Success(
        val shopName: String?,
        val myReview: ReviewUiState?,
        val reviews: Flow<PagingData<ReviewUiState>>
    ) : ShopReviewsScreenState

    data object Loading : ShopReviewsScreenState

    data class Error(val failure: UCResult.Failure) : ShopReviewsScreenState
}
