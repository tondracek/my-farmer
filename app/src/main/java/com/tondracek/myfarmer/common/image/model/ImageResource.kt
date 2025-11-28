package com.tondracek.myfarmer.common.image.model

import android.net.Uri

data class ImageResource(
    val uri: String?,
) {
    constructor(uri: Uri) : this(uri.toString())

    companion object {
        val EMPTY = ImageResource(null)
    }
}