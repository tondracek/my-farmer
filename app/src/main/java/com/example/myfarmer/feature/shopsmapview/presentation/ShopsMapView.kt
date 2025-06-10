package com.example.myfarmer.feature.shopsmapview.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfarmer.feature.shopsmapview.presentation.components.ShopBottomSheet
import com.example.myfarmer.shared.domain.model.ShopId
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShopsMapView(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.initialCameraBounds) {
        val bounds = state.initialCameraBounds ?: return@LaunchedEffect
        delay(500)

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 300)
        )
    }

    val fineLocationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
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
            zoomControlsEnabled = false,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
        )
    ) {
        state.shops.forEach { shop ->
            Marker(
                state = MarkerState(shop.location.toLatLng()),
                onClick = {
                    onShopSelected(shop.id)
                    return@Marker false
                }
            )
        }
    }

    if (state.selectedShop != null) {
        ShopBottomSheet(
            shop = state.shops.first { it.id == state.selectedShop },
            onDismissRequest = { onShopSelected(null) }
        )
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