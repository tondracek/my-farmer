package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.averageRating
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetShopAverageRatingUC @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(shopId: ShopId): Flow<UCResult<Rating>> =
        reviewRepository.getShopReviews(shopId = shopId)
            .map { reviews -> reviews.map { it.rating }.averageRating() }
            .toUCResult(userError = "Couldn't load reviews for this shop.")
}


