package com.tondracek.myfarmer.common.image.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.google.firebase.storage.FirebaseStorage
import com.tondracek.myfarmer.common.image.model.ImageResource
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

enum class PhotoStorageFolder(val path: String) {
    PROFILE_PICTURES("profile_pictures"),
    SHOP_PHOTOS("shop_photos"),
    NONE(""),
}

enum class Quality {
    FULL,
    FULL_HD,
    HD,
}

@Singleton
class PhotoStorage @Inject constructor(
    private val context: Context,
) {

    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String = UUID.randomUUID().toString(),
        folder: PhotoStorageFolder = PhotoStorageFolder.NONE,
        quality: Quality = Quality.FULL,
    ): ImageResource {
        val localUri = imageResource.uri ?: return ImageResource.EMPTY

        // Load bitmap
        val bitmap = loadBitmapFromUri(localUri.toUri()) ?: return ImageResource.EMPTY

        // Resize based on quality
        val resizedBitmap = resizeBitmapForQuality(bitmap, quality)

        // Compress JPEG
        val bytes = compressBitmap(resizedBitmap)

        // Storage path
        val fileName = "$name.jpg"
        val storagePath = when (folder) {
            PhotoStorageFolder.NONE -> fileName
            else -> "${folder.path}/$fileName"
        }

        val ref = storage.reference.child(storagePath)

        // Upload compressed bytes
        ref.putBytes(bytes).await()

        // Get download URL
        val downloadUrl = ref.downloadUrl.await()?.toString()

        return downloadUrl?.let { ImageResource(it) } ?: ImageResource.EMPTY
    }

    suspend fun deletePhoto(imageResource: ImageResource) =
        imageResource.uri
            ?.runCatching {
                storage.reference.child(imageResource.uri)
                    .delete()
                    .await()
            }
            ?.getOrElse {
                Log.e("PHOTO_STORAGE", "Failed to delete photo at ${imageResource.uri}", it)
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
