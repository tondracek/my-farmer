package com.tondracek.myfarmer.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.location.LocationProvider
import javax.inject.Inject

class GetMapViewInitialCameraBoundsUC @Inject constructor(
    private val locationProvider: LocationProvider,
) {

    suspend operator fun invoke(locations: Collection<LatLng>): LatLngBounds? {
        val myLocation = locationProvider.getCurrentLocation()?.toLatLng() ?: return null

        val locations = locations + myLocation
        if (locations.isEmpty()) return null

        val builder = LatLngBounds.builder()
        locations.forEach { builder.include(it) }
        return builder.build()
    }
}