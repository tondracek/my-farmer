package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainShopsScreenViewModel @Inject constructor(
    getShopFilters: GetShopFiltersUC,
    private val navigator: AppNavigator
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    val state: StateFlow<MainShopsScreenState> = filters
        .map { filters -> MainShopsScreenState(filters) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainShopsScreenState(ShopFilters.None),
        )

    fun onOpenFiltersDialog() = navigator.navigate(
        Route.ShopFilterDialog(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)
    )
}
