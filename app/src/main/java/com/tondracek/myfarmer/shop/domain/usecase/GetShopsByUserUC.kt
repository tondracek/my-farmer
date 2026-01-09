package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.flatMapSuccess
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopsByUserUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UCResult<List<Shop>>> =
        getLoggedInUserUC().flatMapSuccess {
            shopRepository.getByOwnerId(ownerId = it.id)
                .toUCResult(userError = "Failed to load user's shops.")
        }
}
