package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllShopsUC @Inject constructor(
    private val shopRepository: ShopRepository,
    private val applyFiltersUC: ApplyFiltersUC,
) {

    operator fun invoke(
        filters: ShopFilters = ShopFilters.None
    ): Flow<UCResult<List<Shop>>> =
        shopRepository.getAll()
            .map { applyFiltersUC(shops = it, filters = filters) }
            .toUCResult(userError = "Failed to load shops.")

    suspend fun paged(
        filters: ShopFilters = ShopFilters.None,
        limit: Int,
        after: ShopId?
    ): UCResult<List<Shop>> = UCResult.of {
        val shops = shopRepository
            .getAll(limit = limit, after = after)
            .first()

        applyFiltersUC(shops = shops, filters = filters)
    }
}