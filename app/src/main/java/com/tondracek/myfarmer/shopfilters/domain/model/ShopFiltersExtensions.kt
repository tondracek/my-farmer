package com.tondracek.myfarmer.shopfilters.domain.model

import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.Shop

suspend fun ShopFilters.apply(
    shops: List<Shop>,
    distanceProvider: suspend (Shop) -> Distance?,
    ratingProvider: (Shop) -> Rating
): List<Shop> {
    return shops.filter { shop ->
        matchesDistance(shop, distanceProvider) &&
                matchesCategory(shop) &&
                matchesRating(shop, ratingProvider)
    }
}

private suspend fun ShopFilters.matchesDistance(
    shop: Shop,
    distanceProvider: suspend (Shop) -> Distance?
): Boolean {
    maxDistanceKm ?: return true
    val dist = distanceProvider(shop) ?: return true
    return dist <= maxDistanceKm
}

private fun ShopFilters.matchesCategory(shop: Shop): Boolean {
    if (categories.isEmpty()) return true
    return shop.categories.any { it.name in categories }
}

private fun ShopFilters.matchesRating(
    shop: Shop,
    ratingProvider: (Shop) -> Rating
): Boolean {
    val rating = ratingProvider(shop)
    return rating >= minRating
}
