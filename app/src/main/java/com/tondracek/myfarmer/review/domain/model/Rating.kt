package com.tondracek.myfarmer.review.domain.model

import kotlin.math.roundToInt

/**
 * @property stars Number of stars in the rating (e.g., 1 to 5)
 */
data class Rating(
    val stars: Int,
) {
    companion object {
        val ZERO = Rating(0)
    }
}

fun Collection<Rating>.averageRating() = when (this.isNotEmpty()) {
    true -> this.map { it.stars }
        .average()
        .roundToInt()
        .let { Rating(it) }

    false -> Rating.ZERO
}