package com.tondracek.myfarmer.common.image.data

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResultList

class FakePhotoStorage() : PhotoStorage {

    val images: MutableMap<String, ImageResource> = mutableMapOf()

    override suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): DomainResult<ImageResource> {
        val path = folder.getPath(name)

        images[path] = imageResource
        return DomainResult.Success(imageResource)
    }

    override suspend fun uploadPhotos(
        imageResources: Collection<Pair<String, ImageResource>>,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): DomainResult<List<ImageResource>> = imageResources.map { (name, imageResource) ->
        uploadPhoto(
            imageResource = imageResource,
            name = name,
            folder = folder,
            quality = quality
        )
    }.toUCResultList()

    override suspend fun deletePhoto(imageResource: ImageResource): DomainResult<Unit> {
        val entry = images.entries.find { it.value == imageResource }?.key

        if (entry != null) images.remove(entry)
        return DomainResult.Success(Unit)
    }

    override suspend fun deletePhotos(imageResources: Collection<ImageResource>): DomainResult<Unit> {
        imageResources.forEach { deletePhoto(it) }
        return DomainResult.Success(Unit)
    }
}