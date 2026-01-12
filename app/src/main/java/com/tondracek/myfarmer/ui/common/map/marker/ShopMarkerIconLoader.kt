package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import android.graphics.Bitmap
import com.tondracek.myfarmer.common.image.model.ImageResource

object ShopMarkerIconLoader {

    private val cache = mutableMapOf<ImageResource, Bitmap>()

    suspend fun get(
        imageResource: ImageResource,
        context: Context,
    ): Bitmap = cache.getOrPut(imageResource) {
        getCustomMarkerIcon(
            imageResource = imageResource,
            context = context
        )
    }
}