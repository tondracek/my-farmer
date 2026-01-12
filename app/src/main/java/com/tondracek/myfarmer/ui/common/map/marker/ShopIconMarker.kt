package com.tondracek.myfarmer.ui.common.map.marker


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.ShopMapItem

@Composable
@GoogleMapComposable
fun ShopIconMarker(
    shop: ShopMapItem,
    onClick: () -> Boolean,
) {
    val context = LocalContext.current

    val icon: BitmapDescriptor by produceState(
        initialValue = BitmapDescriptorFactory.defaultMarker(),
        key1 = shop.icon.uri
    ) { value = BitmapDescriptorFactory.fromBitmap(ShopMarkerIconLoader.get(shop.icon, context)) }

    val state = rememberUpdatedMarkerState(position = shop.location.toLatLng())

    Marker(
        state = state,
        onClick = {
            onClick()
            return@Marker true
        },
        icon = icon,
    )
}