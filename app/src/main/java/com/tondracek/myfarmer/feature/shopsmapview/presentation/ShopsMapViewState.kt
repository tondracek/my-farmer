package com.tondracek.myfarmer.feature.shopsmapview.presentation

import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.shared.domain.model.Shop
import com.tondracek.myfarmer.shared.domain.model.ShopId

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds? = null,
    val shops: Set<Shop> = emptySet(),
    val selectedShop: ShopId? = null,
)
