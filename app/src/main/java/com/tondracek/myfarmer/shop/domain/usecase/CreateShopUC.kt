package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShop
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class CreateShopUC @Inject constructor(
    private val getLoggedInUser: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {

    suspend operator fun invoke(input: ShopInput): UCResult<Unit> =
        UCResult.of("An Error occurred while creating the shop.") {
            val user = getLoggedInUser().first().getOrReturn { return it }

            val shop: Shop = input
                .toShop(shopId = UUID.randomUUID(), ownerId = user.id)
                .getOrReturn { return it }
            shopRepository.create(shop)

            val shopWithPhotos = shop.updatePhotos(shop.id)
            shopRepository.update(shopWithPhotos)
        }

    suspend fun Shop.updatePhotos(shopId: ShopId): Shop = this.copy(
        images = this.images
            .map { UUID.randomUUID().toString() to it }
            .let {
                photoStorage.uploadPhotos(
                    imageResources = it,
                    folder = PhotoStorageFolder.ShopPhotos(shopId),
                    quality = Quality.FULL_HD,
                )
            }
    )
}
