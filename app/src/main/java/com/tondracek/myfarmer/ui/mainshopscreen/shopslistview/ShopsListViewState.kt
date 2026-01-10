package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: List<ShopListViewItem>
    ) : ShopsListViewState()
}