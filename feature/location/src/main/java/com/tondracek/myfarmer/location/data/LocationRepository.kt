package com.tondracek.myfarmer.location.data

import com.tondracek.myfarmer.core.domain.coroutine.AppCoroutineScope
import com.tondracek.myfarmer.location.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    gpsLocationProvider: GpsLocationProvider,
    appScope: AppCoroutineScope,
) {

    private val scope = appScope.scope

    private val gpsFlow = gpsLocationProvider.getCurrentLocation()
        .distinctUntilChanged()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    private val locationFlow = gpsFlow
        .distinctUntilChanged()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    fun getLocation(): Flow<Location?> = locationFlow
    fun getGpsLocation(): Flow<Location?> = gpsFlow
}