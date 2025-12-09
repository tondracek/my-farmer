package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.shop.domain.result.ShopNotFoundUCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteShopUC @Inject constructor(
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(shopId: ShopId): UCResult<Unit> = toUCResult(
        "An error occurred while deleting shop"
    ) {
        val shop = shopRepository.getById(shopId).first()
            ?: return ShopNotFoundUCResult(shopId)

        val currentUser = userRepository.getLoggedInUser().first()
            ?: return NotLoggedInUCResult()

        if (shop.ownerId != currentUser.id)
            return NotShopOwnerUCResult(currentUser.id, shopId)

        val photosToDelete = shop.images
        shopRepository.delete(shopId)
        photoStorage.deletePhotos(photosToDelete)
    }
}