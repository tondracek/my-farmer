package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeReviewRepository : ReviewRepository {

    val items: MutableMap<ReviewId, MutableStateFlow<Review>> = mutableMapOf()

    override suspend fun create(item: Review): UCResult<ReviewId> {
        val id = item.id
        items[id] = MutableStateFlow(item)
        return UCResult.Success(id)
    }

    override suspend fun update(item: Review): UCResult<Unit> {
        items[item.id]?.value = item
        return UCResult.Success(Unit)
    }

    override suspend fun delete(id: ReviewId): UCResult<Unit> {
        items.remove(id)
        return UCResult.Success(Unit)
    }

    override fun getById(id: ReviewId): MutableStateFlow<UCResult<Review>> =
        items[id]
            ?.let { MutableStateFlow(UCResult.Success(it.value)) }
            ?: MutableStateFlow(UCResult.Failure(ReviewError.NotFound))

    override fun getAll(): Flow<UCResult<List<Review>>> =
        combine(items.values) { values ->
            UCResult.Success(values.toList())
        }

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?
    ): Flow<UCResult<List<Review>>> {
        val filtered = items.values
            .filter { it.value.shopId == shopId }
        return combine(filtered) { values ->
            UCResult.Success(values.toList())
        }
    }

    override suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewId?
    ): UCResult<List<Review>> {
        val filtered = items.values
            .map { it.value }
            .filter { it.shopId == shopId }
            .sortedBy { it.id.toString() }

        val startIndex = after?.let { id ->
            filtered.indexOfFirst { it.id == id } + 1
        } ?: 0

        val pagedItems = if (limit > 0) {
            filtered.drop(startIndex).take(limit)
        } else {
            filtered.drop(startIndex)
        }

        return UCResult.Success(pagedItems)
    }
}