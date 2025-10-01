package com.tondracek.myfarmer.ui.shopslistview.presentation

import com.tondracek.myfarmer.ui.shopslistview.presentation.model.ShopListViewItem

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: List<ShopListViewItem> = emptyList()
    ) : ShopsListViewState()
}