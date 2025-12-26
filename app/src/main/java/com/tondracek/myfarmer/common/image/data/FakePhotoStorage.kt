package com.tondracek.myfarmer.common.image.data

import com.tondracek.myfarmer.common.image.model.ImageResource

class FakePhotoStorage() : PhotoStorage {

    val images: MutableMap<String, ImageResource> = mutableMapOf()

    override suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): ImageResource {
        val path = folder.getPath(name)

        images[path] = imageResource
        return imageResource
    }

    override suspend fun uploadPhotos(
        imageResources: Collection<Pair<String, ImageResource>>,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): List<ImageResource> {
        return imageResources.map { (name, imageResource) ->
            uploadPhoto(
                imageResource = imageResource,
                name = name,
                folder = folder,
                quality = quality
            )
        }
    }

    override suspend fun deletePhoto(imageResource: ImageResource) {
        val entry = images.entries.find { it.value == imageResource }?.key

        if (entry != null) images.remove(entry)
    }

    override suspend fun deletePhotos(imageResources: Collection<ImageResource>) {
        imageResources.forEach { deletePhoto(it) }
    }
}