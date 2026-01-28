package com.tondracek.myfarmer.location.domain.usecase

import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.km
import kotlin.math.roundToInt

fun measureMapDistance(pointA: Location?, pointB: Location?): Distance? {
    if (pointA == null || pointB == null) return null

    return measureMapDistanceNotNull(pointA, pointB)
}

fun measureMapDistanceNotNull(pointA: Location, pointB: Location): Distance {
    val myLocation = android.location.Location("myLocation").apply {
        latitude = pointA.latitude
        longitude = pointA.longitude
    }

    val pointLocation = android.location.Location("pointLocation").apply {
        latitude = pointB.latitude
        longitude = pointB.longitude
    }

    val distanceInKm = myLocation.distanceTo(pointLocation) / 1000
    val rounded: Double = (distanceInKm * 100).roundToInt() / 100.0
    return rounded.km
}

