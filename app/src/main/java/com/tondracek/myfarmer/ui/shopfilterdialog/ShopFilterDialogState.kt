package com.tondracek.myfarmer.ui.shopfilterdialog

import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.ui.common.category.CategoryNameInputState

data class ShopFilterDialogState(
    val filters: ShopFilters,

    val categoryNameInputState: CategoryNameInputState,
) {
    companion object {
        val Initial = ShopFilterDialogState(
            filters = ShopFilters.None,
            categoryNameInputState = CategoryNameInputState.Initial,
        )
    }
}