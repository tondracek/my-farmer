package com.example.myfarmer.feature.shopscreen.presentation.mapview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfarmer.shared.domain.ShopId
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun ShopsMapView(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.initialCameraBounds) {
        delay(500)

        val bounds = state.initialCameraBounds ?: return@LaunchedEffect

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 300)
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
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
}

@Preview
@Composable
private fun ShopsMapViewPreview() {
    ShopsMapView(
        state = ShopsMapViewState(),
        onShopSelected = {},
        modifier = Modifier.fillMaxSize(),
    )
}