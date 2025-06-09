package com.example.myfarmer.feature.shopscreen.presentation.mapview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfarmer.feature.shopscreen.presentation.common.ShopId

@Composable
fun ShopsMapView(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
) {
    Box(modifier = modifier) {
        Content(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Color.Cyan))
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