package com.tondracek.myfarmer.shop.domain.repository

import com.tondracek.myfarmer.core.domain.repository.Repository
import com.tondracek.myfarmer.location.model.DistanceRing
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface ShopRepository : Repository<Shop, ShopId> {

    suspend fun getAllPaginated(limit: Int? = null, after: ShopId? = null): List<Shop>

    fun getByOwnerId(ownerId: UserId): Flow<List<Shop>>

    suspend fun getPagedByDistance(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?,
        rings: List<DistanceRing>
    ): Pair<List<Shop>, DistancePagingCursor?>
}

data class DistancePagingCursor(
    val ringIndex: Int,
    val afterGeohash: String?,
)

