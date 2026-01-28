package com.tondracek.myfarmer.location.domain.usecase

import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.km
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

fun measureMapDistance(pointA: Location?, pointB: Location?): Distance? {
    if (pointA == null || pointB == null) return null

    return measureMapDistanceNotNull(pointA, pointB)
}

private const val EARTH_RADIUS_KM = 6371.0
fun measureMapDistanceNotNull(pointA: Location, pointB: Location): Distance {
    val lat1 = Math.toRadians(pointA.latitude)
    val lon1 = Math.toRadians(pointA.longitude)
    val lat2 = Math.toRadians(pointB.latitude)
    val lon2 = Math.toRadians(pointB.longitude)

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = sin(dLat / 2).pow(2) +
            cos(lat1) * cos(lat2) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distanceKm = EARTH_RADIUS_KM * c

    val rounded = (distanceKm * 100).roundToInt() / 100.0
    return rounded.km
}

