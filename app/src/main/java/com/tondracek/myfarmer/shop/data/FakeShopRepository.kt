package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.location.model.DistanceRing
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeShopRepository : ShopRepository {

    private val items: MutableMap<ShopId, MutableStateFlow<Shop>> = mutableMapOf()

    override suspend fun create(item: Shop): ShopId {
        items[item.id] = MutableStateFlow(item)
        return item.id
    }

    override suspend fun update(item: Shop) {
        items[item.id]?.value = item
    }

    override suspend fun delete(id: ShopId) {
        items.remove(id)
    }

    override fun getById(id: ShopId): Flow<Shop?> {
        return items[id] ?: MutableStateFlow(null)
    }

    override fun getAll(): Flow<List<Shop>> =
        combine(items.values) { it.toList() }

    override suspend fun getAllPaginated(
        limit: Int?,
        after: ShopId?
    ): List<Shop> {
        val values = items.values.map { it.value }

        val sortedItems = values.toList()
            .sortedBy { it.id.toString() }

        val startIndex = after?.let { id ->
            sortedItems.indexOfFirst { it.id == id } + 1
        } ?: 0

        val limitedItems = limit?.let {
            sortedItems.drop(startIndex).take(it)
        } ?: sortedItems.drop(startIndex)

        return limitedItems
    }

    override fun getByOwnerId(ownerId: UserId): Flow<List<Shop>> = combine(
        items.values
    ) { values ->
        values.toList().filter { it.ownerId == ownerId }
    }

    override suspend fun getPagedByDistance(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?,
        rings: List<DistanceRing>
    ): Pair<List<Shop>, DistancePagingCursor?> {
        TODO("Not yet implemented")
    }
}