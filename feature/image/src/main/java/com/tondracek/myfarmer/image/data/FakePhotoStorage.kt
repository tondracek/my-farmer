package com.tondracek.myfarmer.image.data

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.toResultList
import com.tondracek.myfarmer.image.model.ImageResource
import java.util.UUID

class FakePhotoStorage() : PhotoStorage {

    val images: MutableMap<String, ImageResource> = mutableMapOf()

    override suspend fun uploadPhoto(
        imageResource: ImageResource,
        folder: PhotoStorageFolder,
        quality: Quality,
    ): DomainResult<ImageResource> {
        val path = folder.getPath(UUID.randomUUID().toString())

        images[path] = imageResource
        return DomainResult.Success(imageResource)
    }

    override suspend fun uploadPhotos(
        imageResources: Collection<ImageResource>,
        folder: PhotoStorageFolder,
        quality: Quality
    ): DomainResult<List<ImageResource>> = imageResources.map { imageResource ->
        uploadPhoto(
            imageResource = imageResource,
            folder = folder,
            quality = quality
        )
    }.toResultList()

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