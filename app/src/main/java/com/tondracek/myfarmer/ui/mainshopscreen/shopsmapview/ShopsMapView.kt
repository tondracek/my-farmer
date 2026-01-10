package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import android.Manifest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.map.marker.ShopIconMarker
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShopsMapView(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId) -> Unit,
) {
    var manuallyZoomed by rememberSaveable { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()

    fun zoomToShop(location: Location, zoom: Float = 15f) = scope.launch {
        val currentZoom = cameraPositionState.position.zoom
        if (currentZoom < zoom) cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(location.toLatLng(), zoom)
        )
    }

    LaunchedEffect(state.initialCameraBounds) {
        if (manuallyZoomed) return@LaunchedEffect
        val bounds = state.initialCameraBounds ?: return@LaunchedEffect
        delay(500)

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 300)
        )
        manuallyZoomed = true
    }

    val fineLocationPermission = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (!fineLocationPermission.status.isGranted) {
            fineLocationPermission.launchPermissionRequest()
        }
    }

    val isLocationGranted = fineLocationPermission.status.isGranted

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = isLocationGranted
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true,
            zoomControlsEnabled = true,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
        ),
        contentPadding = PaddingValues(vertical = MyFarmerTheme.paddings.xxL)
    ) {
        state.shops.forEach { shop ->
            ShopIconMarker(
                shop = shop,
                onClick = {
                    onShopSelected(shop.id)
                    zoomToShop(shop.location)
                    return@ShopIconMarker true
                }
            )
        }
    }
}

@Preview
@Composable
private fun ShopsMapViewPreview() {
    MyFarmerTheme {
        ShopsMapView(
            state = ShopsMapViewState(),
            onShopSelected = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}