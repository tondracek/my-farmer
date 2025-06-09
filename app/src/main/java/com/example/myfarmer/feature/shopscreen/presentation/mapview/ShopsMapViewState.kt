package com.example.myfarmer.feature.shopscreen.presentation.mapview

import com.example.myfarmer.shared.domain.Shop
import com.example.myfarmer.shared.domain.ShopId
import com.google.android.gms.maps.model.LatLngBounds

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds? = null,
    val shops: Set<Shop> = emptySet(),
    val selectedShop: ShopId? = null,
)
