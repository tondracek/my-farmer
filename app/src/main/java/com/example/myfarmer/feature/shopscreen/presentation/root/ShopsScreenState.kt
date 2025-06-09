package com.example.myfarmer.feature.shopscreen.presentation.root

data class ShopsScreenState(
    val viewMode: ShopsViewMode = ShopsViewMode.Map,
)

enum class ShopsViewMode { Map, List }
