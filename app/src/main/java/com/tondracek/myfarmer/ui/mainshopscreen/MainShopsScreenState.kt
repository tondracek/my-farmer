package com.tondracek.myfarmer.ui.mainshopscreen

import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters

data class MainShopsScreenState(
    val filters: ShopFilters = ShopFilters.None,
)