package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.location.model.DistanceRings
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import javax.inject.Inject

class GetShopsByDistancePagedUC @Inject constructor(
    private val shopRepository: ShopRepository,
) {

    suspend operator fun invoke(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?,
    ): UCResult<Pair<List<Shop>, DistancePagingCursor?>> =
        shopRepository.getPagedByDistance(
            center = center,
            pageSize = pageSize,
            cursor = cursor,
            rings = DistanceRings.listViewRings
        )
}