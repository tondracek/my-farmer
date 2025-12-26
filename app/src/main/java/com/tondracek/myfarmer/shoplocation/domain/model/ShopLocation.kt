package com.tondracek.myfarmer.shoplocation.domain.model

import com.google.android.gms.maps.model.LatLng

data class ShopLocation(
    val latitude: Double,
    val longitude: Double,
) {
    constructor(latLng: LatLng) : this(
        latitude = latLng.latitude,
        longitude = latLng.longitude,
    )

    fun toLatLng() = LatLng(latitude, longitude)
}