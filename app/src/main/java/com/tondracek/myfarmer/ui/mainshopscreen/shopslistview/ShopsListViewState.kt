package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.paging.PagingData
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import kotlinx.coroutines.flow.Flow

sealed class ShopsListViewState {
    data object Loading : ShopsListViewState()

    data class Success(
        val shops: List<ShopListViewItem>
    ) : ShopsListViewState()
}