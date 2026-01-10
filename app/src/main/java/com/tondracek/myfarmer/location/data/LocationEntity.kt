package com.tondracek.myfarmer.location.data

import com.tondracek.myfarmer.location.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationEntity(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)

fun LocationEntity.toModel() = Location(
    latitude = latitude,
    longitude = longitude,
)

fun Location.toEntity() = LocationEntity(
    latitude = latitude,
    longitude = longitude,
)