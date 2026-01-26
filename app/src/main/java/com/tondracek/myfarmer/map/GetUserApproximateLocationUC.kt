package com.tondracek.myfarmer.map

import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.usecase.measureMapDistanceNotNull
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

class GetUserApproximateLocationUC @Inject constructor(
    private val getUserLocationUC: GetUserLocationUC,
) {

    operator fun invoke(approximation: Distance) = getUserLocationUC()
        .scan<Location?, Location?>(null) { lastAccepted, current ->
            if (lastAccepted == null) return@scan current
            if (current == null) return@scan lastAccepted

            val d = measureMapDistanceNotNull(lastAccepted, current)

            if (d > approximation) current else lastAccepted
        }
        .distinctUntilChanged()
}