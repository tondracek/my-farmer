package com.tondracek.myfarmer.shared.location.usecase

import com.google.android.gms.maps.model.LatLng
import com.tondracek.myfarmer.shared.location.Distance
import com.tondracek.myfarmer.shared.location.LocationProvider
import javax.inject.Inject

class MeasureDistanceFromMeUC @Inject constructor(
    private val locationProvider: LocationProvider,
    private val measureMapDistance: MeasureMapDistanceUC,
) {
    suspend operator fun invoke(latLng: LatLng?): Distance? {
        val myLocation = locationProvider.getCurrentLocation() ?: return null

        return measureMapDistance(myLocation, latLng)
    }
}