package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.location.domain.usecase.GetUserLocationUC
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.model.apply
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ApplyShopFiltersUC @Inject constructor(
    private val getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    private val getUserLocationUC: GetUserLocationUC,
) {

    operator fun invoke(
        shops: List<Shop>,
        filters: ShopFilters,
    ): Flow<List<Shop>> = combine(
        getUserLocationUC(),
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

    suspend fun sync(
        shops: List<Shop>,
        filters: ShopFilters,
    ): List<Shop> {
        val userLocation = getUserLocationUC().first()
        val ratings = getAverageRatingsByShopUC().getOrElse(emptyMap()).first()

        val distances: Map<ShopId, Distance?> = shops.associate {
            val distance = measureMapDistance(userLocation, it.location)
            it.id to distance
        }

        return filters.apply(
            shops = shops,
            distanceProvider = { shop -> distances[shop.id] },
            ratingProvider = { shop -> ratings[shop.id] ?: Rating.ZERO }
        )
    }
}
