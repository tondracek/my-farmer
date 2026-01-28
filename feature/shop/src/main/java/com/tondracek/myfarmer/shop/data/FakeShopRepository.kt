package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.location.data.GeoHashUtils
import com.tondracek.myfarmer.location.domain.model.DistanceRing
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistanceNotNull
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeShopRepository : ShopRepository {

    private val items: MutableMap<ShopId, MutableStateFlow<Shop>> = mutableMapOf()

    override suspend fun create(item: Shop): DomainResult<ShopId> {
        items[item.id] = MutableStateFlow(item)
        return DomainResult.Success(item.id)
    }

    override suspend fun update(item: Shop): DomainResult<Unit> {
        items[item.id]?.value = item
        return DomainResult.Success(Unit)
    }

    override suspend fun delete(id: ShopId): DomainResult<Unit> {
        items.remove(id)
        return DomainResult.Success(Unit)
    }

    override fun getById(id: ShopId): Flow<DomainResult<Shop>> =
        items[id]
            ?.value
            ?.let { MutableStateFlow(DomainResult.Success(it)) }
            ?: MutableStateFlow(DomainResult.Failure(ShopError.NotFound))

    override fun getAll(): Flow<DomainResult<List<Shop>>> =
        combine(items.values) { values ->
            DomainResult.Success(values.toList())
        }

    override suspend fun getAllPaginated(
        limit: Int?,
        after: ShopId?
    ): DomainResult<List<Shop>> {
        val sortedItems = items.values
            .map { it.value }
            .sortedBy { it.id.toString() }

        val startIndex = after?.let { id ->
            sortedItems.indexOfFirst { it.id == id } + 1
        } ?: 0

        val paginatedItems = if (limit != null) {
            sortedItems.drop(startIndex).take(limit)
        } else {
            sortedItems.drop(startIndex)
        }

        return DomainResult.Success(paginatedItems)
    }

    override fun getByOwnerId(ownerId: UserId): Flow<DomainResult<List<Shop>>> = combine(
        items.values
    ) { values ->
        val filteredItems = values.toList()
            .filter { it.ownerId == ownerId }
        DomainResult.Success(filteredItems)
    }

    /*
    data class DistancePagingCursor(
    val ringIndex: Int,
    val afterGeohash: String?,
)

     */
    override suspend fun getPagedByDistance(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?,
        rings: List<DistanceRing>
    ): DomainResult<Pair<List<Shop>, DistancePagingCursor?>> {
        val ringIndex = cursor?.ringIndex ?: 0
        val ring = rings.getOrNull(ringIndex)
            ?: return DomainResult.Success(Pair(emptyList(), null))
        val startGeohash = cursor?.afterGeohash

        val items = items.values
            .map { it.value }
            .filter { shop ->
                val distance = measureMapDistanceNotNull(center, shop.location)
                distance.toMeters() >= ring.minRadiusMeters &&
                        (ring.maxRadiusMeters?.let { distance.toMeters() <= it } ?: true)
            }
            .sortedBy { measureMapDistanceNotNull(center, it.location) }
            .map { it to GeoHashUtils.encode(it.location.latitude, it.location.longitude) }
            .dropWhile { (_, geohash) ->
                startGeohash?.let { geohash <= it } ?: false
            }
            .take(pageSize + 1)
            .map { it.first }

        val page = items.take(pageSize)

        val nextCursor = when {
            items.size > pageSize -> // More data available in the current ring
                DistancePagingCursor(
                    ringIndex = ringIndex,
                    afterGeohash = GeoHashUtils.encode(
                        page.last().location.latitude,
                        page.last().location.longitude
                    )
                )

            else -> // Move to the next ring
                DistancePagingCursor(
                    ringIndex = ringIndex + 1,
                    afterGeohash = null
                )
        }
        return DomainResult.Success(Pair(page, nextCursor))
    }
}