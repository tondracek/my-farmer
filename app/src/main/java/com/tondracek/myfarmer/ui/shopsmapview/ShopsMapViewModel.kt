package com.tondracek.myfarmer.ui.shopsmapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.shopbottomsheet.navigateToShopBottomSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    getAllShops: GetAllShopsUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(ShopsMapViewState())
    val state: StateFlow<ShopsMapViewState> = combine(
        getAllShops(),
        _state,
    ) { shops, state ->
        state.copy(
            shops = shops.toSet(),
            initialCameraBounds = state.initialCameraBounds
                ?: getLatLngBounds(shops.map { it.location.toLatLng() })
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsMapViewState()
    )

    fun onShopSelected(shopId: ShopId) {
        navigator.navigateToShopBottomSheet(shopId)
    }
}

private fun getLatLngBounds(locations: Collection<LatLng>): LatLngBounds? {
    if (locations.isEmpty()) return null

    val builder = LatLngBounds.builder()
    locations.forEach { builder.include(it) }
    return builder.build()
}