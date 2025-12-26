package com.tondracek.myfarmer.location.usecase

import com.tondracek.myfarmer.location.LocationProvider
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import javax.inject.Inject

class MeasureDistanceFromMeUC @Inject constructor(
    private val locationProvider: LocationProvider,
    private val measureMapDistance: MeasureMapDistanceUC,
) {
    suspend operator fun invoke(location: ShopLocation?): Distance? {
        val myLocation = locationProvider.getCurrentLocation() ?: return null
        if (location == null) return null

        return measureMapDistance(myLocation, Location(location.toLatLng()))
    }
}