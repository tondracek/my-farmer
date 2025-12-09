package com.tondracek.myfarmer.common.image.model

import android.net.Uri

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
}