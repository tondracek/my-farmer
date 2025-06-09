package com.example.myfarmer.feature.shopsmapview.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfarmer.feature.shopsmapview.presentation.components.ShopBottomSheet
import com.example.myfarmer.shared.domain.ShopId
import com.example.myfarmer.shared.theme.MyFarmerTheme
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