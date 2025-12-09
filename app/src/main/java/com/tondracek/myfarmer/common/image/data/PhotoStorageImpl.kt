package com.tondracek.myfarmer.common.image.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.google.firebase.storage.FirebaseStorage
import com.tondracek.myfarmer.common.image.model.ImageResource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min

class PhotoStorageImpl @Inject constructor(
    private val context: Context,
) : PhotoStorage {
    private val storage = FirebaseStorage.getInstance()

    override suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): ImageResource {
        val localUri = imageResource.uri ?: return ImageResource.EMPTY

        val bitmap = loadBitmapFromUri(localUri.toUri()) ?: return ImageResource.EMPTY
        val resizedBitmap = resizeBitmapForQuality(bitmap, quality)
        val bytes = compressBitmap(resizedBitmap)

        val fileName = "$name.jpg"
        val storagePath = folder.getPath(fileName)

        val ref = storage.reference.child(storagePath)

        ref.putBytes(bytes).await()

        return ImageResource(ref.path)
    }

    override suspend fun uploadPhotos(
        imageResources: Collection<Pair<String, ImageResource>>,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): List<ImageResource> = coroutineScope {
        imageResources.map {
            async {
                uploadPhoto(
                    imageResource = it.second,
                    name = it.first,
                    folder = folder,
                    quality = quality
                )
            }
        }.awaitAll()
    }

    override suspend fun deletePhoto(imageResource: ImageResource) {
        imageResource.uri
            ?.runCatching {
                storage.reference.child(imageResource.uri)
                    .delete()
                    .await()
            }
            ?.onFailure {
                Timber.e(it, "Failed to delete photo at ${imageResource.uri}")
            } ?: Timber.e("Photo path is null for imageResource: $imageResource")
    }

    override suspend fun deletePhotos(imageResources: Collection<ImageResource>) {
        coroutineScope {
            imageResources.map { imageResource ->
                async { deletePhoto(imageResource) }
            }.awaitAll()
        }
    }

    /* HELPERS */

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        val originalStream = context.contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(originalStream) ?: return null

        return applyExifRotation(uri, bitmap, context)
    }

    private fun resizeBitmapForQuality(bitmap: Bitmap, quality: Quality): Bitmap {
        return when (quality) {
            Quality.FULL -> bitmap

            Quality.FULL_HD -> {
                val maxWidth = 1920
                val maxHeight = 1080
                val ratio = min(
                    maxWidth.toFloat() / bitmap.width,
                    maxHeight.toFloat() / bitmap.height
                )
                bitmap.scale((bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt())
            }

            Quality.HD -> {
                val maxWidth = 1280
                val maxHeight = 720
                val ratio = min(
                    maxWidth.toFloat() / bitmap.width,
                    maxHeight.toFloat() / bitmap.height
                )
                bitmap.scale((bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt())
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
        return output.toByteArray()
    }

    private fun applyExifRotation(uri: Uri, bitmap: Bitmap, context: Context): Bitmap {
        val input = context.contentResolver.openInputStream(uri) ?: return bitmap
        val exif = ExifInterface(input)

        val rotation = when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        if (rotation == 0f) return bitmap

        val matrix = Matrix().apply { postRotate(rotation) }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}
