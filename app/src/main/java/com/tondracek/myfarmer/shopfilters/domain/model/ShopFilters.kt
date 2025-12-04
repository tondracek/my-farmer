package com.tondracek.myfarmer.shopfilters.domain.model

import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import java.util.SortedSet

data class ShopFilters(
    val maxDistanceKm: Distance?,
    val categories: SortedSet<String>,
    val minRating: Rating
) {
    companion object {
        val None = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf(),
            minRating = Rating.ZERO
        )
    }
}
