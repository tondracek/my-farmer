package com.tondracek.myfarmer.location.domain.usecase

import com.tondracek.myfarmer.location.data.LocationRepository
import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.meters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import timber.log.Timber
import javax.inject.Inject

class GetUserApproximateLocationUC @Inject constructor(
    private val locationRepository: LocationRepository,
) {

    operator fun invoke(approximation: Distance = 50.meters) =
        locationRepository.getLocation()
            .filter(approximation)
            .onEach {
                Timber.d("User approximate location updated: $it")
            }

    fun getGpsLocation(approximation: Distance = 50.meters) =
        locationRepository.getGpsLocation()
            .filter(approximation)
            .onEach {
                Timber.d("User GPS-only approximate location updated: $it")
            }

    fun Flow<Location?>.filter(approximation: Distance) = this
        .runningReduce { lastAccepted, current ->
            when {
                lastAccepted == null -> current
                current == null -> lastAccepted
                else -> {
                    val d = measureMapDistanceNotNull(lastAccepted, current)
                    if (d > approximation) current else lastAccepted
                }
            }
        }
        .distinctUntilChanged()
}