package com.example.myfarmer.feature.shopslistview.presentation

import com.example.myfarmer.feature.shopslistview.presentation.model.ShopListViewItem

data class ShopsListViewState(
    val shops: List<ShopListViewItem> = emptyList()
)
