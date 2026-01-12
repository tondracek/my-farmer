package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.shop.domain.result.ShopNotFoundUCResult
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class UpdateShopUC @Inject constructor(
    private val getLoggedInUser: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(shopId: ShopId, input: ShopInput): UCResult<Unit> =
        UCResult.of("An error occurred while updating shop") {
            val user = getLoggedInUser().first().getOrReturn { return it }

            val originalShop = shopRepository.getById(shopId).first()
                ?: return ShopNotFoundUCResult(shopId)

            if (originalShop.ownerId != user.id)
                return NotShopOwnerUCResult(user.id, shopId)

            val originalImages = originalShop.images
            val updatedShop = input
                .toShop(shopId = shopId, ownerId = originalShop.ownerId)
                .getOrReturn { return it }
                .updatePhotos(originalImages = originalImages)

            shopRepository.update(updatedShop)

            return UCResult.Success(Unit)
        }

    private suspend fun Shop.updatePhotos(originalImages: List<ImageResource>): Shop {
        val photosToUpload = images.filter { it !in originalImages }
        val uploadedPhotos = photoStorage.uploadPhotos(
            imageResources = photosToUpload.map { UUID.randomUUID().toString() to it },
            folder = PhotoStorageFolder.ShopPhotos(this.id),
            quality = Quality.FULL_HD,
        )

        val photosToDelete = originalImages.filter { it !in images }
        val photosToKeep = originalImages.filter { it in images }
        photoStorage.deletePhotos(photosToDelete)

        val newImages = photosToKeep + uploadedPhotos

        return this.copy(images = newImages)
    }
}