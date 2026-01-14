package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.mapFlow
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.averageRating
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopAverageRatingUC @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(shopId: ShopId): Flow<DomainResult<Rating>> =
        reviewRepository.getShopReviews(shopId = shopId)
            .mapFlow { reviews -> reviews.map { it.rating }.averageRating() }
}


