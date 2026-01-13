package com.tondracek.myfarmer.review.domain.repository

import com.tondracek.myfarmer.core.domain.repository.Repository
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface ReviewRepository : Repository<Review, ReviewId> {

    fun getShopReviews(
        shopId: ShopId,
        limit: Int? = null,
    ): Flow<UCResult<List<Review>>>

    suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewId?
    ): UCResult<List<Review>>

    fun getUserReviewOnShop(shopId: ShopId, userId: UserId): Flow<UCResult<Review>>
}