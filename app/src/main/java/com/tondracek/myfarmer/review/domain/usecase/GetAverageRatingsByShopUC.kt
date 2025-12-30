package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.toAverageRating
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAverageRatingsByShopUC @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(): Flow<UCResult<Map<ShopId, Rating>>> = reviewRepository.getAll()
        .map { list ->
            list.groupBy { it.shopId }
                .mapValues { (_, reviews) ->
                    reviews.toAverageRating()
                }
        }
        .toUCResult(userError = "Couldn't load reviews for shops.")
}