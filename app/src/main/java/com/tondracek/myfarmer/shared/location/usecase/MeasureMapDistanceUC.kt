package com.tondracek.myfarmer.shared.location.usecase

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.tondracek.myfarmer.shared.location.Distance
import com.tondracek.myfarmer.shared.location.km
import javax.inject.Inject
import kotlin.math.roundToInt

class MeasureMapDistanceUC @Inject constructor(
) : suspend (LatLng?, LatLng?) -> Distance? {

    override suspend operator fun invoke(pointA: LatLng?, pointB: LatLng?): Distance? {
        if (pointA == null || pointB == null) return null

        val myLocation = Location("myLocation").apply {
            latitude = pointA.latitude
            longitude = pointA.longitude
        }

        val pointLocation = Location("pointLocation").apply {
            latitude = pointB.latitude
            longitude = pointB.longitude
        }

        val distanceInKm = myLocation.distanceTo(pointLocation) / 1000
        val rounded: Double = (distanceInKm * 100).roundToInt() / 100.0
        return rounded.km
    }
}
