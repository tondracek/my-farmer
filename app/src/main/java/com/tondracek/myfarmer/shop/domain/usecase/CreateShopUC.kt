package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.data.FirebaseAuthRepository
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageFolder
import com.tondracek.myfarmer.common.image.data.Quality
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.result.MissingShopInputDataUCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class CreateShopUC @Inject constructor(
    private val shopRepository: ShopRepository,
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
    private val photoStorage: PhotoStorage,
) {
    suspend operator fun invoke(input: ShopInput): UCResult<Unit> {
        val currentUserId = authRepository.getCurrentUserFirebaseId().first()
            ?: return NotLoggedInUCResult()
        val user = userRepository.getUserByFirebaseId(currentUserId).first()
            ?: return NotLoggedInUCResult()

        val shopId = UUID.randomUUID()
        val shop: Shop = input.toShop(shopId = shopId, ownerId = user.id)
            .getOrReturn { return it }
            .updatePhotos(shopId = shopId)

        return shopRepository.create(shop)
            .let { UCResult.Success(Unit) }
    }

    suspend fun Shop.updatePhotos(shopId: ShopId): Shop = this.apply {
        this.images
            .map { UUID.randomUUID().toString() to it }
            .let {
                photoStorage.uploadPhotos(
                    imageResources = it,
                    folder = PhotoStorageFolder.ShopPhotos(shopId),
                    quality = Quality.FULL_HD,
                )
            }
    }

    private fun ShopInput.toShop(
        shopId: ShopId,
        ownerId: UUID,
    ): UCResult<Shop> {
        if (this.menu == null || this.location == null || this.openingHours == null)
            return MissingShopInputDataUCResult

        return Shop(
            id = shopId,
            name = this.name,
            description = this.description,
            ownerId = ownerId,
            categories = this.categories,
            images = this.images,
            menu = this.menu,
            location = this.location,
            openingHours = this.openingHours,
        ).let { UCResult.Success(it) }
    }
}
