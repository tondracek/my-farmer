package com.tondracek.myfarmer.ui.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.ui.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    measureDistanceFromMe: MeasureDistanceFromMeUC,
    getAllShops: GetAllShopsUC,
) : ViewModel() {

    private val _state = MutableStateFlow(ShopsListViewState.Loading)

    val state: StateFlow<ShopsListViewState> = combine(
        getAllShops(),
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