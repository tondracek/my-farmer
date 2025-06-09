package com.example.myfarmer.feature.shopscreen.presentation.listview

import androidx.lifecycle.ViewModel
import com.example.myfarmer.feature.shopscreen.presentation.common.ShopId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(ShopsListViewState())
    val state: StateFlow<ShopsListViewState> = _state

    fun navigateToShopDetail(shopId: ShopId) {
        println("TODO: Navigate to shop $shopId detail")
    }
}