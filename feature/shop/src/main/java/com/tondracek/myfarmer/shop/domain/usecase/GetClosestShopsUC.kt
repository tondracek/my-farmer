package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.location.data.GpsLocationProvider
import com.tondracek.myfarmer.location.data.LocationRepository
import com.tondracek.myfarmer.location.domain.usecase.GetUserApproximateLocationUC
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.shop.domain.model.Shop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetClosestShopsUC @Inject constructor(
    private val getUserApproximateLocationUC: GetUserApproximateLocationUC,
) {
    suspend operator fun invoke(
        shops: Collection<Shop>,
        count: Int
    ): List<Shop> {
        if (shops.isEmpty()) return emptyList()

        val currentLocation = getUserApproximateLocationUC().firstOrNull()
        return shops
            .sortedBy { measureMapDistance(currentLocation, it.location) }
            .take(count)
    }
}
