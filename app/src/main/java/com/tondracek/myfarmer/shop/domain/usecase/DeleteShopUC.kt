package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.shop.domain.result.ShopNotFoundUCResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteShopUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(shopId: ShopId): UCResult<Unit> = UCResult.of(
        userError = "An error occurred while deleting shop"
    ) {
        val currentUser = getLoggedInUserUC().first().getOrReturn { return it }
        val shop = shopRepository.getById(shopId).first() ?: return ShopNotFoundUCResult(shopId)
        if (shop.ownerId != currentUser.id)
            return NotShopOwnerUCResult(currentUser.id, shopId)

        val photosToDelete = shop.images
        photoStorage.deletePhotos(photosToDelete)
        shopRepository.delete(shopId)
    }
}