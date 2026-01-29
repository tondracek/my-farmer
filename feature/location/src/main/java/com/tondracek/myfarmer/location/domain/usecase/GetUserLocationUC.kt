package com.tondracek.myfarmer.location.domain.usecase

import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.LocationProvider
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

    operator fun invoke(): Flow<Location?> = flow {
        while (currentCoroutineContext().isActive) {
            val location = locationProvider.getCurrentLocation()

            emit(location)

            delay(INTERVAL)
        }
    }.distinctUntilChanged()
}