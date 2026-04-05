package com.tondracek.myfarmer.location.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IpLocationApi @Inject constructor(
    private val client: OkHttpClient
) {

    private fun fetchRaw(): String? {
        val request = Request.Builder()
            .url("https://ipinfo.io/json")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            return response.body.string()
        }
    }

    suspend fun getLatLng(): Pair<Double, Double>? =
        withContext(Dispatchers.IO) {
            try {
                val body = fetchRaw() ?: return@withContext null
                val json = JSONObject(body)

                val loc = json.optString("loc")
                val parts = loc.split(",")

                if (parts.size != 2) return@withContext null

                val lat = parts[0].toDouble()
                val lng = parts[1].toDouble()

                lat to lng
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch location from IP API")
                null
            }
        }
}