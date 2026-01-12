package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetAllShopsUC @Inject constructor(
    private val shopRepository: ShopRepository,
    private val applyFiltersUC: ApplyFiltersUC,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        filters: ShopFilters = ShopFilters.None
    ): Flow<UCResult<List<Shop>>> =
        shopRepository.getAll()
            .flatMapLatest { applyFiltersUC(shops = it, filters = filters) }
            .toUCResult(userError = "Failed to load shops.")
}

class GetAllShopsPaginatedUC @Inject constructor(
    private val shopRepository: ShopRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(
        limit: Int? = null,
        after: ShopId? = null,
    ): UCResult<List<Shop>> = UCResult.of("Failed to load shops.") {
        shopRepository.getAllPaginated(
            limit = limit,
            after = after,
        )
    }
}