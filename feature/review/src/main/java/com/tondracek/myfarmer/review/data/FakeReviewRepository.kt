package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.data.firestore.domainresult.toDomainResultNonNull
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewPageCursor
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeReviewRepository : ReviewRepository {

    val items: MutableMap<ReviewId, MutableStateFlow<Review>> = mutableMapOf()

    override suspend fun create(item: Review): DomainResult<ReviewId> {
        val id = item.id
        items[id] = MutableStateFlow(item)
        return DomainResult.Success(id)
    }

    override suspend fun update(item: Review): DomainResult<Unit> {
        items[item.id]?.value = item
        return DomainResult.Success(Unit)
    }

    override suspend fun delete(id: ReviewId): DomainResult<Unit> {
        items.remove(id)
        return DomainResult.Success(Unit)
    }

    override fun getById(id: ReviewId): MutableStateFlow<DomainResult<Review>> =
        items[id]
            ?.let { MutableStateFlow(DomainResult.Success(it.value)) }
            ?: MutableStateFlow(DomainResult.Failure(ReviewError.NotFound))

    override fun getAll(): Flow<DomainResult<List<Review>>> =
        combine(items.values) { values ->
            DomainResult.Success(values.toList())
        }

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?
    ): Flow<DomainResult<List<Review>>> {
        val filtered = items.values
            .filter { it.value.shopId == shopId }
        return combine(filtered) { values ->
            DomainResult.Success(values.toList())
        }
    }

    override suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewPageCursor?
    ): DomainResult<Pair<List<Review>, ReviewPageCursor?>> {
        TODO()
    }

    override fun getUserReviewOnShop(
        shopId: ShopId,
        userId: UserId,
    ): Flow<DomainResult<Review>> = combine(items.values) { values ->
        values.firstOrNull { it.shopId == shopId && it.userId == userId }
    }.toDomainResultNonNull(ReviewError.NotFound, ReviewError.FetchingFailed)

}