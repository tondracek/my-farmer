package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import com.google.android.gms.maps.model.BitmapDescriptor
import com.tondracek.myfarmer.common.image.model.ImageResource

object ShopMarkerIconLoader {

    private val cache = mutableMapOf<ImageResource, BitmapDescriptor>()

    suspend fun get(
        imageResource: ImageResource,
        context: Context,
    ): BitmapDescriptor = cache.getOrPut(imageResource) {
        getCustomMarkerIcon(
            imageResource = imageResource,
            context = context
        )
    }
}