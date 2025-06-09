package com.example.myfarmer.feature.shopscreen.presentation.mapview

import com.example.myfarmer.feature.shopscreen.presentation.common.Shop
import com.example.myfarmer.feature.shopscreen.presentation.common.ShopId

data class ShopsMapViewState(
    val shops: Set<Shop> = emptySet(),
    val selectedShop: ShopId? = null,
)
