package com.tondracek.myfarmer.review.domain.model

import kotlin.math.roundToInt

data class Rating(
    val stars: Int, // e.g., 1 to 5
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