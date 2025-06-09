package com.example.myfarmer.feature.shopscreen.presentation.root

data class ShopsScreenState(
    val viewMode: ShopViewMode = ShopViewMode.Map,
)

enum class ShopViewMode { Map, List }
