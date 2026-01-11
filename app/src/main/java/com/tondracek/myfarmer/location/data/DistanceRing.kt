package com.tondracek.myfarmer.location.data

import timber.log.Timber

data class DistanceRing(
    val minRadiusMeters: Double,
    val maxRadiusMeters: Double,
)

object DistanceRings {

    val rings: List<DistanceRing> = listOf(
        DistanceRing(0.0, 500.0),
        DistanceRing(500.0, 1_000.0),
        DistanceRing(1_000.0, 2_000.0),
        DistanceRing(2_000.0, 5_000.0),
        DistanceRing(5_000.0, 10_000.0),
    )

    operator fun get(index: Int): DistanceRing =
        rings.getOrElse(index) {
            Timber.e("No distance ring at index $index. Available rings: ${rings.size}")
            rings.last()
        }
}
