package com.tondracek.myfarmer.review.domain.repository

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow

interface ReviewRepository : Repository<Review, ReviewId> {

    fun getReviewsPreview(shopId: ShopId): Flow<List<Review>>

    fun getShopReviews(shopId: ShopId): Flow<List<Review>>
}