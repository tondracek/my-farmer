package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeReviewRepository : ReviewRepository {

    val items: MutableMap<ReviewId, MutableStateFlow<Review>> = mutableMapOf()

    override suspend fun create(item: Review): ReviewId {
        items[item.id] = MutableStateFlow(item)
        return item.id
    }

    override suspend fun update(item: Review) {
        items[item.id]?.value = item
    }

    override suspend fun delete(id: ReviewId) {
        items.remove(id)
    }

    override fun getById(id: ReviewId): Flow<Review?> =
        items[id] ?: MutableStateFlow(null)

    override fun getAll(): Flow<List<Review>> =
        combine(items.values) { it.toList() }

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?,
        after: ReviewId?
    ): Flow<List<Review>> = combine(
        items.values
    ) { values ->
        val sortedItems = values.toList()
            .filter { it.shopId == shopId }
            .sortedBy { it.id.toString() }

        val startIndex = after?.let { id ->
            sortedItems.indexOfFirst { it.id == id } + 1
        } ?: 0

        val limitedItems = limit?.let {
            sortedItems.drop(startIndex).take(it)
        } ?: sortedItems.drop(startIndex)

        limitedItems
    }
}