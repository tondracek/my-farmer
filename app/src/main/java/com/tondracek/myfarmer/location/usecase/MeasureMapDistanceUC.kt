package com.tondracek.myfarmer.location.usecase

import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.model.km
import javax.inject.Inject
import kotlin.math.roundToInt

class MeasureMapDistanceUC @Inject constructor(
) : suspend (Location?, Location?) -> Distance? {

    override suspend operator fun invoke(pointA: Location?, pointB: Location?): Distance? {
        if (pointA == null || pointB == null) return null

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
}
