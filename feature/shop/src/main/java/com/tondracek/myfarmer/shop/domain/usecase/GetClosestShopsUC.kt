package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.location.domain.model.LocationProvider
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.shop.domain.model.Shop
import javax.inject.Inject

class GetClosestShopsUC @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(
        shops: Collection<Shop>,
        count: Int
    ): List<Shop> {
        if (shops.isEmpty()) return emptyList()

        val currentLocation = locationProvider.getCurrentLocation()
        return shops
            .sortedBy { measureMapDistance(currentLocation, it.location) }
            .take(count)
    }
}
