package com.tondracek.myfarmer.common.model

data class ImageResource(
    val uri: String?,
) {
    companion object {
        val EMPTY = ImageResource(null)
    }
}