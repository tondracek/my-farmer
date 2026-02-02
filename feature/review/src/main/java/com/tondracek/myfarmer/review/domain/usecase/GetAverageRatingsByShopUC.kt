package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.mapFlow
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.toAverageRating
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAverageRatingsByShopUC @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(): Flow<DomainResult<Map<ShopId, Rating>>> = reviewRepository.getAll()
        .mapFlow { list ->
            list.groupBy { it.shopId }
                .mapValues { (_, reviews) ->
                    reviews.toAverageRating()
                }
        }
}