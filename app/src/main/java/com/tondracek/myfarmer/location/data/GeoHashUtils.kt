package com.tondracek.myfarmer.location.data

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.tondracek.myfarmer.location.model.Location

object GeoHashUtils {

    fun encode(latitude: Double, longitude: Double): String =
        GeoFireUtils.getGeoHashForLocation(
            GeoLocation(latitude, longitude)
        )

    fun ranges(
        center: Location,
        radiusMeters: Double,
    ): List<GeoHashRange> {

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
}


data class GeoHashRange(
    val start: String,
    val end: String,
)


