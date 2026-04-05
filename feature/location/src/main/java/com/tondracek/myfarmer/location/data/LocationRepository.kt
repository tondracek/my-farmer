package com.tondracek.myfarmer.location.data

import com.tondracek.myfarmer.core.domain.coroutine.AppCoroutineScope
import com.tondracek.myfarmer.location.domain.model.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val IP_INTERVAL = 60.minutes
private val IP_RETRY_INTERVAL = 20.seconds
private val IP_CACHE_TTL = 60.minutes.inWholeMilliseconds

@Singleton
class LocationRepository @Inject constructor(
    gpsLocationProvider: GpsLocationProvider,
    private val ipLocationApi: IpLocationApi,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ipFlow = gpsFlow
        .flatMapLatest { gps ->
            if (gps != null) {
                flowOf(null)
            } else {
                flow<Location?> {
                    while (currentCoroutineContext().isActive) {
                        val location = retrieveIpLocation()
                        emit(location)

                        delay(
                            if (location == null) IP_RETRY_INTERVAL
                            else IP_INTERVAL
                        )
                    }
                }
            }
        }
        .distinctUntilChanged()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    @Volatile
    private var cachedIpLocation: Location? = null

    @Volatile
    private var cacheTimestamp: Long = 0L

    private suspend fun retrieveIpLocation(): Location? {
        val now = System.currentTimeMillis()

        cachedIpLocation?.let {
            if (now - cacheTimestamp < IP_CACHE_TTL) {
                return it
            }
        }

        val location = ipLocationApi.getLatLng()
            ?.let { (lat, lng) -> Location(lat, lng) }

        if (location != null) {
            cachedIpLocation = location
            cacheTimestamp = now
        }

        return location
    }

    private val locationFlow = combine(gpsFlow, ipFlow) { gps, ip ->
        gps ?: ip
    }
        .distinctUntilChanged()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    fun getLocation(): Flow<Location?> = locationFlow
    fun getGpsLocation(): Flow<Location?> = gpsFlow
}