package com.example.myfarmer.shared.domain

import com.google.android.gms.maps.model.LatLng

data class ShopLocation(
    val latitude: Double,
    val longitude: Double
) {
    fun toLatLng() = LatLng(latitude, longitude)
}
