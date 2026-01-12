package com.tondracek.myfarmer.location.data

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.tondracek.myfarmer.location.model.DistanceRing
import com.tondracek.myfarmer.location.model.Location

object GeoHashUtils {

    fun encode(latitude: Double, longitude: Double): String =
        GeoFireUtils.getGeoHashForLocation(
            GeoLocation(latitude, longitude)
        )

    private fun ranges(
        center: Location,
        radiusMeters: Double?,
    ): List<GeoHashRange> {
        if (radiusMeters == null) return listOf(
            GeoHashRange(
                start = "",
                end = "~" // '~' is after all valid geohash characters
            )
        )

        val centerGeoLocation = GeoLocation(
            center.latitude,
            center.longitude
        )

        return GeoFireUtils
            .getGeoHashQueryBounds(centerGeoLocation, radiusMeters)
            .map { bound ->
                GeoHashRange(
                    start = bound.startHash,
                    end = bound.endHash
                )
            }
    }

    fun ranges(
        center: Location,
        ring: DistanceRing,
    ): List<GeoHashRange> {

        val outer = ranges(center, ring.maxRadiusMeters)

        if (ring.minRadiusMeters <= 0.0) {
            return outer
        }

        val inner = ranges(center, ring.minRadiusMeters)

        return subtractRanges(outer, inner)
    }
}

fun subtractRanges(
    outer: List<GeoHashRange>,
    inner: List<GeoHashRange>
): List<GeoHashRange> {

    var result = outer.toList()

    for (innerRange in inner) {
        result = result.flatMap { outerRange ->
            subtractSingleRange(outerRange, innerRange)
        }
    }

    return result
}

private fun subtractSingleRange(
    outer: GeoHashRange,
    inner: GeoHashRange
): List<GeoHashRange> {

    // No overlap
    if (inner.end <= outer.start || inner.start >= outer.end) {
        return listOf(outer)
    }

    val result = mutableListOf<GeoHashRange>()

    // Left remainder
    if (inner.start > outer.start) {
        result += GeoHashRange(
            start = outer.start,
            end = minOf(inner.start, outer.end)
        )
    }

    // Right remainder
    if (inner.end < outer.end) {
        result += GeoHashRange(
            start = maxOf(inner.end, outer.start),
            end = outer.end
        )
    }

    return result
}

data class GeoHashRange(
    val start: String,
    val end: String,
)
