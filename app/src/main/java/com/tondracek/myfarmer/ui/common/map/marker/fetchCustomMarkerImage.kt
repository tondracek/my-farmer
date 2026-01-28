package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
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
    if (imageUrl == null) return AppCompatResources.getDrawable(context, fallback)!!

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false)
        .build()

    return when (val imageResult = context.imageLoader.execute(imageRequest)) {
        is ErrorResult -> AppCompatResources.getDrawable(context, fallback)!!
        is SuccessResult -> imageResult.image.asDrawable(context.resources)
    }
}