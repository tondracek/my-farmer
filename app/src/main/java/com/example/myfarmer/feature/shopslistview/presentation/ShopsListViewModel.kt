package com.example.myfarmer.feature.shopslistview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfarmer.feature.shopslistview.presentation.model.toListItem
import com.example.myfarmer.shared.domain.Shop
import com.example.myfarmer.shared.domain.ShopId
import com.example.myfarmer.shared.domain.sampleShops
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
) : ViewModel() {

    private val _shops: Flow<List<Shop>> = flowOf(sampleShops + sampleShops + sampleShops)
    private val _state = MutableStateFlow(ShopsListViewState())

    val state: StateFlow<ShopsListViewState> = combine(
        _shops,
        _state
    ) { shops, state ->
        state.copy(shops = shops.map { it.toListItem() })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsListViewState()
    )

    fun navigateToShopDetail(shopId: ShopId) {
        /* TODO: Navigate to shop shopId detail */
    }
}