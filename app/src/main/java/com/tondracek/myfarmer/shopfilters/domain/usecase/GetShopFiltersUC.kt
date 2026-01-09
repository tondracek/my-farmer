package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetShopFiltersUC @Inject constructor(
    private val filterRepositoryFactory: ShopFilterRepositoryFactory
) {
    operator fun invoke(repositoryKey: String): StateFlow<ShopFilters> {
        return filterRepositoryFactory.createOrGet(repositoryKey).filters
    }
}
