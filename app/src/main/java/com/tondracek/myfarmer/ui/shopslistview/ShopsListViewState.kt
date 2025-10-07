package com.tondracek.myfarmer.ui.shopslistview

import com.tondracek.myfarmer.ui.shopslistview.components.ShopListViewItem

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: List<ShopListViewItem> = emptyList()
    ) : ShopsListViewState()
}