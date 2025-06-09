package com.example.myfarmer.feature.shopscreen.presentation.listview

import com.example.myfarmer.shared.domain.Shop

data class ShopsListViewState(
    val shops: List<Shop> = emptyList()
)
