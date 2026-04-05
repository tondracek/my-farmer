package com.tondracek.myfarmer.location.data

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tondracek.myfarmer.location.domain.model.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GpsLocationProvider @Inject constructor(
    private val context: Context
) {

    private val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun getCurrentLocation(): Flow<Location?> = callbackFlow {

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5_000L
        )
            .setMinUpdateIntervalMillis(2_000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation

                trySend(
                    loc?.let {
                        Location(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                )
            }
        }

        try {
            client.lastLocation.addOnSuccessListener {
                trySend(it?.let { Location(it.latitude, it.longitude) })
            }

            client.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )
        } catch (_: SecurityException) {
            trySend(null)
            close()
            return@callbackFlow
        }

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}