package com.tondracek.myfarmer.common.image.model

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

data class ImageResource(
    val uri: String?,
) {
    constructor(uri: Uri) : this(uri.toString())

    companion object {
        val EMPTY = ImageResource(null)
    }

    fun isFirebaseStoragePath(): Boolean = when {
        uri.isNullOrEmpty() -> false
        uri.startsWith("content://") -> false
        uri.startsWith("file://") -> false
        uri.startsWith("http://") -> false
        uri.startsWith("https://") -> false
        else -> true
    }

    suspend fun getImageUrl(): String? {
        if (!isFirebaseStoragePath()) return uri

        val path = uri ?: return null

        FirebaseUrlCache.get(path)?.let { return it }

        return try {
            val url = Firebase.storage
                .reference
                .child(path)
                .downloadUrl
                .await()
                .toString()

            FirebaseUrlCache.put(path, url)
            url
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Failed to get image URL for path: $uri")
            null
        }
    }
}