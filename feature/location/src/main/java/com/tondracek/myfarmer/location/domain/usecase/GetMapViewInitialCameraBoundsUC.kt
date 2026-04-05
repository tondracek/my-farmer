package com.tondracek.myfarmer.location.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetMapViewInitialCameraBoundsUC @Inject constructor(
    private val getUserApproximateLocationUC: GetUserApproximateLocationUC,
) {

    suspend operator fun invoke(locations: Collection<LatLng>): LatLngBounds? {
        val myLocation = getUserApproximateLocationUC.getGpsLocation().firstOrNull()?.toLatLng()
        Timber.d("[GetMapViewInitialCameraBoundsUC] User approximate location: $myLocation")

        val locations = when (myLocation) {
            null -> locations
            else -> locations + myLocation
        }
        if (locations.isEmpty()) return null

        val builder = LatLngBounds.builder()
        locations.forEach { builder.include(it) }
        return builder.build()
    }
}