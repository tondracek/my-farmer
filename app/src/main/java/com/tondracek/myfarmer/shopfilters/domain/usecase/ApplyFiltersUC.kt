package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.usecase.measureMapDistance
import com.tondracek.myfarmer.map.GetUserLocationUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.model.apply
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ApplyFiltersUC @Inject constructor(
    private val getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    private val getUserLocationUC: GetUserLocationUC,
) {

    operator fun invoke(
        shops: List<Shop>,
        filters: ShopFilters,
    ): Flow<List<Shop>> = combine(
        getUserLocationUC().getOrElse(null),
        getAverageRatingsByShopUC().getOrElse(emptyMap()),
    ) { userLocation, ratings ->
        val distances: Map<ShopId, Distance?> = shops.associate {
            val distance = measureMapDistance(userLocation, it.location)
            it.id to distance
        }

        filters.apply(
            shops = shops,
            distanceProvider = { shop -> distances[shop.id] },
            ratingProvider = { shop -> ratings[shop.id] ?: Rating.ZERO }
        )
    }
}
