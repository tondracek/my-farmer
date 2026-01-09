package com.tondracek.myfarmer.ui.common.shop.filter

import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepository
import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory


object ShopFiltersRepositoryKeys {
    const val MAIN_SHOPS_SCREEN = "MainShopsScreenFilterRepository"
}

fun ShopFilterRepositoryFactory.getMainShopsScreenFilterRepository(): ShopFilterRepository =
    createOrGet(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)