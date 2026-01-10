package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    suspend fun paged(
        filters: ShopFilters = ShopFilters.None,
        limit: Int,
        after: ShopId?
    ): UCResult<List<Shop>> = UCResult.of {
        val shops = shopRepository
            .getAll(limit = limit, after = after)
            .first()

        applyFiltersUC(shops = shops, filters = filters).first()
    }
}
