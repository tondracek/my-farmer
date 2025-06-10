package com.example.myfarmer.feature.shopsmapview.presentation

import com.example.myfarmer.shared.domain.model.Shop
import com.example.myfarmer.shared.domain.model.ShopId
import com.google.android.gms.maps.model.LatLngBounds

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds? = null,
    val shops: Set<Shop> = emptySet(),
    val selectedShop: ShopId? = null,
)
