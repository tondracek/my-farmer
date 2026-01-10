package com.tondracek.myfarmer.review.domain.repository

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow

interface ReviewRepository : Repository<Review, ReviewId> {

    fun getShopReviews(
        shopId: ShopId,
        limit: Int? = null,
        after: ReviewId? = null
    ): Flow<List<Review>>
}