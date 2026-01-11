package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.paging.compose.LazyPagingItems
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: LazyPagingItems<ShopListViewItem>
    ) : ShopsListViewState()
}