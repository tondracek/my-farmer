package com.tondracek.myfarmer.ui.common.map.mapbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap

@Composable
fun MapboxMapView(
    modifier: Modifier = Modifier,
    onMapReady: (MapView, MapboxMap) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                onMapReady(this, mapboxMap)
            }
        }
    )
}

