package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsPreviewUC @Inject constructor(
    private val repository: ReviewRepository
) {

    operator fun invoke(shopId: ShopId): Flow<UCResult<List<Review>>> =
        repository.getShopReviews(shopId, limit = 3)

}