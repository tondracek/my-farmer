package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.model.apply
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ApplyFiltersUC @Inject constructor(
    private val measureDistanceFromMeUC: MeasureDistanceFromMeUC,
    private val getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
) {

    suspend operator fun invoke(
        shops: List<Shop>,
        filters: ShopFilters,
    ): List<Shop> {
        val averageRatingsByShop: Map<ShopId, Rating> =
            getAverageRatingsByShopUC().first().getOrElse(emptyMap())

        val shopIdToDistanceRating: Map<ShopId, Pair<Distance?, Rating>> = coroutineScope {
            shops.map {
                async {
                    val distance = measureDistanceFromMeUC(it.location)
                    val rating = averageRatingsByShop.getOrElse(it.id) { Rating.ZERO }

                    it.id to (distance to rating)
                }
            }
                .awaitAll()
                .toMap()
        }

        return filters.apply(
            shops = shops,
            distanceProvider = { shop -> shopIdToDistanceRating[shop.id]?.first },
            ratingProvider = { shop -> shopIdToDistanceRating[shop.id]?.second ?: Rating.ZERO }
        )
    }
}
