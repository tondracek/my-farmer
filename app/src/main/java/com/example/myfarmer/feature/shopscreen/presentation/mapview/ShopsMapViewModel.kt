package com.example.myfarmer.feature.shopscreen.presentation.mapview

import androidx.lifecycle.ViewModel
import com.example.myfarmer.feature.shopscreen.presentation.common.ShopId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShopsMapViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(ShopsMapViewState())
    val state: StateFlow<ShopsMapViewState> = _state

    fun onShopSelected(shopId: ShopId?) {
        _state.update { it.copy(selectedShop = shopId) }
    }
}