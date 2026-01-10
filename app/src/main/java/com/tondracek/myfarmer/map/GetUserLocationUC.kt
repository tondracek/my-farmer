package com.tondracek.myfarmer.map

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.location.LocationProvider
import com.tondracek.myfarmer.location.model.Location
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

private val INTERVAL = 5.seconds

class GetUserLocationUC @Inject constructor(
    private val locationProvider: LocationProvider
) {

    operator fun invoke(): Flow<UCResult<Location>> = flow {
        while (currentCoroutineContext().isActive) {
            locationProvider.getCurrentLocation()
                ?.let { emit(it) }
            delay(INTERVAL)
        }
    }
        .toUCResult("Couldn't get user's location")
        .distinctUntilChanged()

}