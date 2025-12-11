package com.tondracek.myfarmer.common.image.model

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

data class ImageResource(
    val uri: String?,
) {
    constructor(uri: Uri) : this(uri.toString())

    companion object {
        val EMPTY = ImageResource(null)
    }

    fun isFirebaseStoragePath(): Boolean {
        return when {
            uri.isNullOrEmpty() -> false
            uri.startsWith("content://") -> false
            uri.startsWith("file://") -> false
            uri.startsWith("http://") -> false
            uri.startsWith("https://") -> false
            else -> true
        }
    }

    suspend fun getImageUrl(): String? {
        if (!isFirebaseStoragePath()) return uri

        val path = uri ?: return null

        FirebaseUrlCache.get(path)?.let { return it }

        val url = Firebase.storage
            .reference
            .child(path)
            .downloadUrl
            .await()
            .toString()

        FirebaseUrlCache.put(path, url)

        return url
    }
}