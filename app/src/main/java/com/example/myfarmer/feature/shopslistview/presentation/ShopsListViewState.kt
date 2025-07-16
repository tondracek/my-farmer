package com.example.myfarmer.feature.shopslistview.presentation

import com.example.myfarmer.feature.shopslistview.presentation.model.ShopListViewItem

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: List<ShopListViewItem> = emptyList()
    ) : ShopsListViewState()
}