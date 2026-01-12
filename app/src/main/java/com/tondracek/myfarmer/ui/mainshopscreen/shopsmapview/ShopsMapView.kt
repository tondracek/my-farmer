package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLngBounds
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.map.mapbox.CLUSTER_LAYER_ID
import com.tondracek.myfarmer.ui.common.map.mapbox.MapboxMapView
import com.tondracek.myfarmer.ui.common.map.mapbox.PROP_ID
import com.tondracek.myfarmer.ui.common.map.mapbox.PROP_POINT_COUNT
import com.tondracek.myfarmer.ui.common.map.mapbox.SHOP_LAYER_ID
import com.tondracek.myfarmer.ui.common.map.mapbox.SHOP_SOURCE_ID
import com.tondracek.myfarmer.ui.common.map.mapbox.addShopLayers
import com.tondracek.myfarmer.ui.common.map.mapbox.shopsToFeatureCollection
import com.tondracek.myfarmer.ui.common.map.mapbox.toStyleIconId
import com.tondracek.myfarmer.ui.common.map.marker.ShopMarkerIconLoader
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.max

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShopsMapView(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId) -> Unit,
) {
    val fineLocationPermission = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (!fineLocationPermission.status.isGranted) {
            fineLocationPermission.launchPermissionRequest()
        }
    }

    var mapboxMapView by remember { mutableStateOf<MapView?>(null) }
    var mapboxMap by remember { mutableStateOf<MapboxMap?>(null) }

    Box(modifier = modifier) {
        MapboxShopMap(
            state = state,
            onShopSelected = onShopSelected,
            mapboxMapView = mapboxMapView,
            mapboxMap = mapboxMap,
            mapViewReadyCallback = { mapView, map ->
                mapboxMapView = mapView
                mapboxMap = map
            }
        )
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 16.dp, horizontal = 4.dp),
            shape = RoundedCornerShape(64.dp),
            color = MyFarmerTheme.colors.surfaceContainer,
            tonalElevation = 4.dp,
        ) {
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    val mapboxMapView = mapboxMapView ?: return@IconButton
                    val mapboxMap = mapboxMap ?: return@IconButton

                    zoomToUser(mapboxMapView, mapboxMap)
                },
                colors = MyFarmerTheme.iconButtonColors.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My location"
                )
            }
        }
    }

    var hasSetInitialBounds by remember { mutableStateOf(false) }
    LaunchedEffect(state.initialCameraBounds, mapboxMap) {
        if (hasSetInitialBounds) return@LaunchedEffect

        val initialCameraBounds = state.initialCameraBounds ?: return@LaunchedEffect
        val mapboxMap = mapboxMap ?: return@LaunchedEffect

        initialCameraBounds.let {
            mapboxMap.zoomToBounds(it)
            hasSetInitialBounds = true
        }
    }
}

@Composable
private fun MapboxShopMap(
    modifier: Modifier = Modifier,
    state: ShopsMapViewState,
    onShopSelected: (ShopId) -> Unit,
    mapboxMapView: MapView?,
    mapboxMap: MapboxMap?,
    mapViewReadyCallback: (MapView, MapboxMap) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    MapboxMapView(
        modifier = modifier.fillMaxSize(),
        onMapReady = { mapView, map ->
            mapViewReadyCallback(mapView, map)

            mapView.gestures.rotateEnabled = false
            mapView.gestures.pitchEnabled = false

            mapView.location.apply {
                enabled = true
                pulsingEnabled = false
                locationPuck = LocationPuck2D(
                    bearingImage = ImageHolder.from(R.drawable.outline_my_location_24),
                    shadowImage = ImageHolder.from(R.drawable.mapbox_user_puck_shadow),
                )
            }

            map.loadStyle(Style.MAPBOX_STREETS) { style ->
                map.addOnMapClickListener { point ->
                    handleShopClick(map, point, onShopSelected)
                    true
                }
                addShopLayers(style, state.shops)
            }
        }
    )

    LaunchedEffect(state.shops) {
        val mapboxMapView = mapboxMapView ?: return@LaunchedEffect

        mapboxMap?.getStyle { style ->
            val context = mapboxMapView.context

            coroutineScope.launch {
                state.shops
                    .map { it.icon }
                    .distinct()
                    .forEach { icon ->
                        val imageId = icon.toStyleIconId()

                        if (!style.hasStyleImage(imageId)) {
                            val bitmap = ShopMarkerIconLoader.get(icon, context)
                            style.addImage(imageId, bitmap)
                        }
                    }

                updateShopSource(style, state.shops)
            }
        }
    }
}

fun updateShopSource(
    style: Style,
    shops: Collection<ShopMapItem>
) {
    style.getSourceAs<GeoJsonSource>(SHOP_SOURCE_ID)
        ?.featureCollection(shopsToFeatureCollection(shops))
}

private fun handleShopClick(
    mapboxMap: MapboxMap,
    point: Point,
    onShopSelected: (ShopId) -> Unit
) {
    val geometry = RenderedQueryGeometry(
        mapboxMap.pixelForCoordinate(point)
    )

    val options = RenderedQueryOptions(
        listOf(
            CLUSTER_LAYER_ID,
            SHOP_LAYER_ID
        ),
        null
    )

    mapboxMap.queryRenderedFeatures(geometry, options) { result ->
        result.onValue { features ->
            val feature = features.firstOrNull()?.queriedFeature?.feature ?: return@onValue

            when {
                // CLUSTER CLICK
                feature.hasProperty(PROP_POINT_COUNT) -> zoomIntoCluster(mapboxMap, feature)

                // SHOP CLICK
                else -> feature.getStringProperty(PROP_ID)
                    ?.let(ShopId::fromString)
                    ?.let(onShopSelected)
                    ?.also {
                        zoomToShop(mapboxMap, feature)
                    }
            }
        }

        result.onError {
            Timber.e(it, "Mapbox feature query failed")
        }
    }
}

private fun zoomIntoCluster(
    mapboxMap: MapboxMap,
    feature: Feature,
) {
    val geometry = feature.geometry() as? Point ?: return
    val currentZoom = mapboxMap.cameraState.zoom

    mapboxMap.easeTo(
        CameraOptions.Builder()
            .center(geometry)
            .zoom(currentZoom + 2.0)
            .build(),
        MapAnimationOptions.mapAnimationOptions { duration(500) }
    )
}

private fun zoomToShop(
    mapboxMap: MapboxMap,
    feature: Feature,
) {
    val geometry = feature.geometry() as? Point ?: return

    mapboxMap.easeTo(
        CameraOptions.Builder()
            .center(geometry)
            .zoom(max(15.5, mapboxMap.cameraState.zoom)) // good “nearby shops” zoom
            .build(),
        MapAnimationOptions.mapAnimationOptions { duration(500) }
    )
}

fun MapboxMap.zoomToBounds(bounds: LatLngBounds) {
    val mapboxBounds = CoordinateBounds(
        Point.fromLngLat(bounds.southwest.longitude, bounds.southwest.latitude),
        Point.fromLngLat(bounds.northeast.longitude, bounds.northeast.latitude)
    )

    easeTo(
        CameraOptions.Builder()
            .center(mapboxBounds.center())
            .zoom(12.0)
            .build(),
        MapAnimationOptions.mapAnimationOptions { duration(500) }
    )
}

private fun zoomToUser(
    mapView: MapView,
    mapboxMap: MapboxMap,
) {
    val listener = object : (Point) -> Unit {
        override fun invoke(point: Point) {
            val userPoint = Point.fromLngLat(
                point.longitude(),
                point.latitude()
            )

            mapboxMap.easeTo(
                CameraOptions.Builder()
                    .center(userPoint)
                    .zoom(15.5) // good “nearby shops” zoom
                    .build(),
                MapAnimationOptions.mapAnimationOptions {
                    duration(500)
                }
            )

            mapView.location.removeOnIndicatorPositionChangedListener(this)
        }
    }

    mapView.location.addOnIndicatorPositionChangedListener(listener)
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