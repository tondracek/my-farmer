package com.tondracek.myfarmer.location.model


data class DistanceRing(
    val minRadiusMeters: Double,
    val maxRadiusMeters: Double?,
)

object DistanceRings {

    val listViewRings = listOf(
        DistanceRing(0.0, 3_000.0),         // my town
        DistanceRing(3_000.0, 8_000.0),     // nearby villages
        DistanceRing(8_000.0, 20_000.0),    // surrounding area
        DistanceRing(20_000.0, 50_000.0),   // region
        DistanceRing(50_000.0, 100_000.0),  // state
        DistanceRing(100_000.0, 250_000.0), // country
        DistanceRing(250_000.0, null),       // worldwide
    )

    val mapViewRings = listOf(
        DistanceRing(0.0, 3_000.0),         // my town
        DistanceRing(3_000.0, 8_000.0),     // nearby villages
        DistanceRing(8_000.0, 20_000.0),    // surrounding area
        DistanceRing(20_000.0, 50_000.0),   // region
        DistanceRing(50_000.0, 100_000.0),  // state
        DistanceRing(100_000.0, 250_000.0), // country
        DistanceRing(250_000.0, 500_000.0),
        DistanceRing(500_000.0, null),
    )
}
