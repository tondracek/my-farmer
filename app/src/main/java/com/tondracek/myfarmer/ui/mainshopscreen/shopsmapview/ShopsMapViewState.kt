package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId

data class ShopsMapViewState(
    val initialCameraBounds: LatLngBounds?,
    val selectedShop: ShopMapItem?,
    val isLoading: Boolean,
    val shops: Set<ShopMapItem>,
) {
    companion object {
        val Empty = ShopsMapViewState(
            initialCameraBounds = null,
            selectedShop = null,
            shops = emptySet(),
            isLoading = false,
        )
    }
}

data class ShopMapItem(
    val id: ShopId,
    val name: String?,
    val location: Location,
    val icon: ImageResource,
)

fun Shop.toShopMapItem(icon: ImageResource) = ShopMapItem(
    id = this.id,
    name = this.name,
    location = this.location,
    icon = icon,
)