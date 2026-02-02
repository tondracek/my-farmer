package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.flatMap
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopsByUserUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<DomainResult<List<Shop>>> =
        getLoggedInUserUC().flatMap {
            shopRepository.getByOwnerId(ownerId = it.id)
        }
}
