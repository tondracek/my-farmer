package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    factory: RepositoryCoreFactory,
    mapper: ReviewMapper,
) : BaseRepository<Review>(
    factory.create(mapper, ReviewEntity::class.java)
) {

    fun getReviewsPreview(shopId: ShopId): Flow<List<Review>> = repositoryRequest {
        addFilters(ReviewEntity::shopId filterEq shopId.toString())
        setLimit(3)
    }.let { this.get(it) }

    fun getReviews(shopId: ShopId, offset: Int, limit: Int): Flow<List<Review>> =
        repositoryRequest {
            addFilters(ReviewEntity::shopId filterEq shopId.toString())
            setOffset(offset)
            setLimit(limit)
        }.let { this.get(it) }
}
