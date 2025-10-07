package com.tondracek.myfarmer.ui.shopsmapview

import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds? = null,
    val shops: Set<Shop> = emptySet(),
    val selectedShop: ShopId? = null,
)
