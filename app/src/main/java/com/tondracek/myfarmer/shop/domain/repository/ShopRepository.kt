package com.tondracek.myfarmer.shop.domain.repository

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface ShopRepository : Repository<Shop, ShopId> {

    fun getAll(limit: Int? = null, after: ShopId? = null): Flow<List<Shop>>

    fun getByOwnerId(ownerId: UserId): Flow<List<Shop>>

    fun getPagedByDistance(
        /* TODO */
    ): Flow<List<Shop>>
}
