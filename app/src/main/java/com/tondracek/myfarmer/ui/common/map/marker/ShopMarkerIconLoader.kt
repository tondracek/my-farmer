package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import com.tondracek.myfarmer.image.model.ImageResource

object ShopMarkerIconLoader {

    private val cache = mutableMapOf<ImageResource, Bitmap>()

    suspend fun get(
        imageResource: ImageResource,
        context: Context,
        color: Color,
    ): Bitmap = cache.getOrPut(imageResource) {
        getCustomMarkerIcon(
            imageResource = imageResource,
            context = context,
            color = color
        )
    }
}