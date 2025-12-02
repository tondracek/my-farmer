package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory
import javax.inject.Inject

class GetShopFilterRepositoryUC @Inject constructor(
    private val filterRepositoryFactory: ShopFilterRepositoryFactory
) {
    operator fun invoke(repositoryKey: String) =
        filterRepositoryFactory.createOrGet(repositoryKey)
}