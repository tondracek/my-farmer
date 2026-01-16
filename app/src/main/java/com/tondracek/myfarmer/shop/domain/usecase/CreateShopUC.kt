package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateShopUC @Inject constructor(
    private val getLoggedInUser: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {

    suspend operator fun invoke(input: ShopInput): DomainResult<Unit> {
        val user = getLoggedInUser().first()
            .getOrReturn { return it }

        val shop: Shop = input
            .toShop(shopId = ShopId.newId(), ownerId = user.id)
            .getOrReturn { return it }

        shopRepository.create(shop)
            .getOrReturn { return it }

        val shopWithPhotos = shop.updatePhotos(shop.id)
            .getOrReturn { return it }

        return shopRepository.update(shopWithPhotos)
    }

    /**
     * - Uploads the photos for the shop
     * - retrieves their newly generated URLs
     * - updates the shop with the new photo URLs
     */
    private suspend fun Shop.updatePhotos(shopId: ShopId): DomainResult<Shop> {
        val newImages = photoStorage.uploadPhotos(
            imageResources = this.images,
            folder = PhotoStorageFolder.ShopPhotos(shopId),
            quality = Quality.FULL_HD,
        ).getOrReturn { return it }

        return this.copy(images = newImages)
            .let { DomainResult.Success(it) }
    }
}
