package com.tondracek.myfarmer.review.domain.repository

import com.tondracek.myfarmer.core.domain.repository.Repository
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface ReviewRepository : Repository<Review, ReviewId> {

    fun getShopReviews(
        shopId: ShopId,
        limit: Int? = null,
    ): Flow<DomainResult<List<Review>>>

    suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewPageCursor?,
    ): DomainResult<Pair<List<Review>, ReviewPageCursor?>>

    fun getUserReviewOnShop(shopId: ShopId, userId: UserId): Flow<DomainResult<Review>>
}

data class ReviewPageCursor(
    val createdAt: Instant,
    val id: ReviewId
)