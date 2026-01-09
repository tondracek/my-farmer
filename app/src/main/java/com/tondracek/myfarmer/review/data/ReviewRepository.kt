package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.IdMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    core: RepositoryCore<ReviewEntity, FirestoreEntityId>,
    mapper: ReviewMapper,
) : BaseRepository<Review, ReviewId, ReviewEntity, FirestoreEntityId>(
    core = core,
    entityMapper = mapper,
    idMapper = object : IdMapper<ReviewId, FirestoreEntityId> {
        override fun toEntityId(modelId: ReviewId) = modelId.toString()
        override fun toModelId(entityId: FirestoreEntityId) = ReviewId.fromString(entityId)
    }
) {

    fun getReviewsPreview(shopId: ShopId): Flow<List<Review>> = repositoryRequest {
        addFilters(ReviewEntity::shopId filterEq shopId.toString())
        setLimit(3)
    }.let { this.get(it) }

    fun getReviews(shopId: ShopId, offset: Int? = null, limit: Int? = null): Flow<List<Review>> =
        repositoryRequest {
            addFilters(ReviewEntity::shopId filterEq shopId.toString())
            offset?.let { setOffset(offset) }
            limit?.let { setLimit(limit) }
        }.let { this.get(it) }
}
