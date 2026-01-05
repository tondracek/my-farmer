package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds? = null,
    val shops: Set<ShopMapItem> = emptySet(),
)

data class ShopMapItem(
    val id: ShopId,
    val name: String?,
    val location: ShopLocation,
    val icon: ImageResource,
)

fun Shop.toShopMapItem(icon: ImageResource) = ShopMapItem(
    id = this.id,
    name = this.name,
    location = this.location,
    icon = icon,
)