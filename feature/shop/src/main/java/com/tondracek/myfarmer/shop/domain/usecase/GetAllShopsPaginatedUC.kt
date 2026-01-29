package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import javax.inject.Inject

class GetAllShopsPaginatedUC @Inject constructor(
    private val shopRepository: ShopRepository,
) {

    suspend operator fun invoke(
        limit: Int? = null,
        after: ShopId? = null,
    ): DomainResult<List<Shop>> =
        shopRepository.getAllPaginated(limit = limit, after = after)
}