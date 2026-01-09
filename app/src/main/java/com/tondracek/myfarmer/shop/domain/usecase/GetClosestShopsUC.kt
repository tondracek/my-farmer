package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import javax.inject.Inject

class GetClosestShopsUC @Inject constructor(
    private val measureDistanceFromMeUC: MeasureDistanceFromMeUC
) {
    suspend operator fun invoke(
        shops: Collection<Shop>,
        count: Int
    ): List<Shop> = shops
        .map { it to measureDistanceFromMeUC(it.location) }
        .sortedBy { it.second }
        .map { it.first }
        .take(count)
}
