package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.image.data.PhotoStorage
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteShopUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(shopId: ShopId): DomainResult<Unit> {
        val currentUser = getLoggedInUserUC().first()
            .getOrReturn { return it }
        val shop = shopRepository.getById(shopId).first()
            .getOrReturn { return it }
        if (shop.ownerId != currentUser.id)
            return DomainResult.Failure(ShopError.NotOwner)

        val photosToDelete = shop.images
        photoStorage.deletePhotos(photosToDelete).getOrReturn { return it }
        return shopRepository.delete(shopId)
    }
}