package com.tondracek.myfarmer.ui.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    measureDistanceFromMe: MeasureDistanceFromMeUC
) : ViewModel() {
    private val _shops: Flow<List<Shop>> = flowOf(sampleShops + sampleShops + sampleShops)
    private val _state = MutableStateFlow(ShopsListViewState.Loading)

    val state: StateFlow<ShopsListViewState> = combine(
        _shops,
        _state
    ) { shops, _ ->
        val shopListItems = shops.map {
            val distance = measureDistanceFromMe(it.location.toLatLng())
            it.toListItem(distance)
        }

        ShopsListViewState.Success(shops = shopListItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsListViewState.Loading
    )

    fun navigateToShopDetail(shopId: ShopId) {
        /* TODO: Navigate to shop shopId detail */
    }
}