package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.location.LocationProvider
import com.tondracek.myfarmer.location.usecase.measureMapDistance
import com.tondracek.myfarmer.shop.domain.model.Shop
import javax.inject.Inject

class GetClosestShopsUC @Inject constructor(
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(
        shops: Collection<Shop>,
        count: Int
    ): List<Shop> = shops
        .map {
            val currentLocation = locationProvider.getCurrentLocation()
            it to measureMapDistance(currentLocation, it.location)
        }
        .sortedBy { it.second }
        .map { it.first }
        .take(count)
}
