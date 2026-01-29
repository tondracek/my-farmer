package com.tondracek.myfarmer.shopfilters.data

import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShopFilterRepository() {
    private val filtersState = MutableStateFlow(ShopFilters.None)
    val filters: StateFlow<ShopFilters> = filtersState

    fun updateFilters(filters: ShopFilters) {
        filtersState.value = filters
    }

    fun resetFilters() {
        filtersState.value = ShopFilters.None
    }
}
