package com.tondracek.myfarmer.location.usecase

import com.google.android.gms.maps.model.LatLng
import com.tondracek.myfarmer.location.LocationProvider
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import javax.inject.Inject

class MeasureDistanceFromMeUC @Inject constructor(
    private val locationProvider: LocationProvider,
    private val measureMapDistance: MeasureMapDistanceUC,
) {
    suspend operator fun invoke(latLng: LatLng?): Distance? { // TODO: Change to custom Location class
        val myLocation = locationProvider.getCurrentLocation() ?: return null
        if (latLng == null) return null

        return measureMapDistance(myLocation, Location(latLng))
    }
}