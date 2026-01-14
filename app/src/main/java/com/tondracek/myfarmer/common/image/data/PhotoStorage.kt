package com.tondracek.myfarmer.common.image.data

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import java.util.UUID

enum class Quality {
    FULL,
    FULL_HD,
    HD,
}

interface PhotoStorage {

    suspend fun uploadPhoto(
        imageResource: ImageResource,
        name: String = UUID.randomUUID().toString(),
        folder: PhotoStorageFolder = PhotoStorageFolder.None,
        quality: Quality = Quality.FULL,
    ): DomainResult<ImageResource>

    suspend fun uploadPhotos(
        imageResources: Collection<Pair<String, ImageResource>>,
        folder: PhotoStorageFolder = PhotoStorageFolder.None,
        quality: Quality = Quality.FULL,
    ): DomainResult<List<ImageResource>>

    suspend fun deletePhoto(imageResource: ImageResource): DomainResult<Unit>
    suspend fun deletePhotos(imageResources: Collection<ImageResource>): DomainResult<Unit>
}