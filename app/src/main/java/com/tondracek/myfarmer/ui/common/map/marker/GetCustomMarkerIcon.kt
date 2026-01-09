package com.tondracek.myfarmer.ui.common.map.marker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.withClip
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.farmerDarkColors

suspend fun getCustomMarkerIcon(
    imageResource: ImageResource,
    context: Context,
): BitmapDescriptor {
    val imageUrl = imageResource.getImageUrl()
    val drawable: Drawable =
        fetchCustomMarkerImage(context, imageUrl, R.drawable.baseline_store_48)

    val imageBitmap: Bitmap = drawable.toBitmapSafely()

    val height = 220
    val width = 150
    val output = createBitmap(width, height)
    val canvas = Canvas(output)

    val imageSize = width

    // Location Icon Path
    val anchorPath = Path().apply {
        addArc(0f, 0f, imageSize.toFloat(), imageSize.toFloat(), 180f, 360f)
        moveTo(canvas.width / 2f, canvas.height.toFloat())
        lineTo(0f, imageSize.toFloat() / 2f)
        lineTo(canvas.width.toFloat(), imageSize.toFloat() / 2f)
    }

    canvas.drawPath(anchorPath, Paint().apply {
        color = farmerDarkColors.primaryContainer.toArgb()
        style = Paint.Style.FILL
    })

    canvas.withClip(
        Path().apply { addCircle(width * .5f, width * .5f, width * .4f, Path.Direction.CW) }
    ) {
        val originalSize = (imageBitmap.width + imageBitmap.height) / 2
        val ratio = originalSize.toFloat() / imageSize.toFloat()
        val scaledBitmap = imageBitmap.scale(
            width = (imageBitmap.width / ratio).toInt(),
            height = (imageBitmap.height / ratio).toInt()
        )
        canvas.drawBitmap(scaledBitmap, 0f, 0f, Paint().apply { isAntiAlias = true })
    }

    return BitmapDescriptorFactory.fromBitmap(output)
}

private fun Drawable.toBitmapSafely(): Bitmap {
    if (this is BitmapDrawable) return this.bitmap

    val bitmap = createBitmap(
        intrinsicWidth.takeIf { it > 0 } ?: 1,
        intrinsicHeight.takeIf { it > 0 } ?: 1
    )

    val canvas = Canvas(bitmap)

    val xOffset = canvas.width / 10
    val yOffset = canvas.height / 10
    setBounds(xOffset, yOffset, canvas.width - xOffset, canvas.height - yOffset)
    draw(canvas)
    return bitmap
}
