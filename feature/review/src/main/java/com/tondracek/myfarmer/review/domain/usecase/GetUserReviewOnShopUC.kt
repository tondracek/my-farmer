package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.user.domain.model.UserId
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserReviewOnShopUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    operator fun invoke(shopId: ShopId, userId: UserId): Flow<DomainResult<Review>> =
        reviewRepository.getUserReviewOnShop(shopId, userId)
}
