package com.tondracek.myfarmer.shoplocation.data

import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import kotlinx.serialization.Serializable

@Serializable
data class ShopLocationEntity(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)

fun ShopLocationEntity.toModel() = ShopLocation(
    latitude = latitude,
    longitude = longitude,
)

fun ShopLocation.toEntity() = ShopLocationEntity(
    latitude = latitude,
    longitude = longitude,
)