package com.example.myfarmer.feature.shopscreen.presentation.listview

import com.example.myfarmer.feature.shopscreen.presentation.common.Shop

data class ShopsListViewState(
    val shops: List<Shop> = emptyList()
)
