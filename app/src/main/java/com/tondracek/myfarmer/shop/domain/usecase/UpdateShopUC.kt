package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.core.domain.usecaseresult.mapSuccess
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResultList
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateShopUC @Inject constructor(
    private val getLoggedInUser: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(shopId: ShopId, input: ShopInput): DomainResult<Unit> {
        val user = getLoggedInUser().first().getOrReturn { return it }

        val originalShop = shopRepository.getById(shopId).first().getOrReturn { return it }

        if (originalShop.ownerId != user.id)
            return DomainResult.Failure(ShopError.NotOwner)

        val originalImages = originalShop.images
        val updatedShop = input
            .toShop(shopId = shopId, ownerId = originalShop.ownerId)
            .getOrReturn { return it }
            .updatePhotos(originalImages = originalImages)
            .getOrReturn { return it }

        return shopRepository.update(updatedShop)
    }

    private suspend fun Shop.updatePhotos(originalImages: List<ImageResource>): DomainResult<Shop> {
        val shopId = this.id

        val photosToDelete = originalImages.filter { it !in images }
        photoStorage.deletePhotos(photosToDelete).getOrReturn { return it }

        val newImages = coroutineScope {
            images.mapIndexed { i, image ->
                async {
                    when {
                        image in originalImages -> DomainResult.Success(image)

                        else -> photoStorage.uploadPhoto(
                            imageResource = image,
                            folder = PhotoStorageFolder.ShopPhotos(shopId),
                            quality = Quality.FULL_HD,
                        )
                    }
                }
            }.awaitAll().toUCResultList()
        }

        return newImages.mapSuccess { this.copy(images = it) }
    }
}