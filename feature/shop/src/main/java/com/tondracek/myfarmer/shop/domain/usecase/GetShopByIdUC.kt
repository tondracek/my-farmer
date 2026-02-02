package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShopByIdUC @Inject constructor(
    private val repository: ShopRepository,
) {

    operator fun invoke(id: ShopId): Flow<DomainResult<Shop>> =
        repository.getById(id)
}
