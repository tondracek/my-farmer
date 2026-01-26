package com.tondracek.myfarmer.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tondracek.myfarmer.location.model.Location
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class LocationProvider @Inject constructor(
    private val context: Context
) {

    private val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun getCurrentLocation(): Location? = try {
        val token = CancellationTokenSource()

        val location: android.location.Location = client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            token.token
        ).await()

        Location(
            latitude = location.latitude,
            longitude = location.longitude
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: SecurityException) {
        null
    } catch (e: Exception) {
        null
    }
}
