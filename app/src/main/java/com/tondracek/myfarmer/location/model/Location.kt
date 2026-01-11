package com.tondracek.myfarmer.location.model

import com.google.android.gms.maps.model.LatLng

/**
 * Custom My Farmer location data class.
 *
 * @param latitude The latitude of the location.
 * @param longitude The longitude of the location.
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val geohash: String? = null
) {
    constructor(latLng: LatLng) : this(
        latitude = latLng.latitude,
        longitude = latLng.longitude,
    )

    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}
