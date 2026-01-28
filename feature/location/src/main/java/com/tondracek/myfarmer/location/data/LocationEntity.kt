package com.tondracek.myfarmer.location.data

import com.tondracek.myfarmer.location.domain.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationEntity(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var geohash: String = "",
)

fun LocationEntity.toModel() = Location(
    latitude = latitude,
    longitude = longitude,
)

fun Location.toEntity() = LocationEntity(
    latitude = latitude,
    longitude = longitude,
    geohash = GeoHashUtils.encode(latitude, longitude),
)