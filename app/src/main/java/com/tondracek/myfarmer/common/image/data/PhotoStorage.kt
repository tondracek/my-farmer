package com.tondracek.myfarmer.common.image.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.tondracek.myfarmer.common.image.model.ImageResource
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

enum class PhotoStorageFolder(val path: String) {
    PROFILE_PICTURES("profile_pictures"),
    SHOP_PHOTOS("shop_photos"),
    NONE(""),
}

@Singleton
class PhotoStorage @Inject constructor() {

    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String = UUID.randomUUID().toString(),
        folder: PhotoStorageFolder = PhotoStorageFolder.NONE
    ): ImageResource {
        if (imageResource.uri == null) return ImageResource.EMPTY

        val fileName = "$name.jpg"
        val storagePath = when (folder) {
            PhotoStorageFolder.NONE -> fileName
            else -> "${folder.path}/$fileName"
        }

        val ref = storage.reference.child(storagePath)

        ref.putFile(imageResource.uri.toUri())
            .await()

        val downloadUrl: Uri? = ref.downloadUrl.await()
        return downloadUrl
            ?.let { ImageResource(downloadUrl.toString()) }
            ?: ImageResource.EMPTY
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
}
