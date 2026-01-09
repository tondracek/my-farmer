package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import coil3.asDrawable
import coil3.imageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware

suspend fun fetchCustomMarkerImage(
    context: Context,
    imageUrl: String?,
    @DrawableRes fallback: Int
): Drawable {
    if (imageUrl == null) return context.getDrawable(fallback)!!

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false)
        .build()

    val imageResult = context.imageLoader.execute(imageRequest)

    return when (imageResult) {
        is ErrorResult -> context.getDrawable(fallback)!!
        is SuccessResult -> imageResult.image.asDrawable(context.resources)
    }
}