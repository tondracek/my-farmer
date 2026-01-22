package com.tondracek.myfarmer.ui.createshopflow.common.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFormEvent
import kotlinx.coroutines.launch

@Composable
fun ShopFlowNameLocationStep(
    shopInput: ShopInput,
    onShopFormEvent: (ShopFormEvent) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = shopInput.name,
            onValueChange = { onShopFormEvent(ShopFormEvent.UpdateName(it)) },
            singleLine = true,
            label = { Text(stringResource(R.string.please_enter_your_shop_s_name)) },
        )

        Card(
            colors = MyFarmerTheme.cardColors.primary,
            shape = RoundedCornerShape(MyFarmerTheme.paddings.medium * 1.5f),
        ) {
            Column(
                modifier = Modifier.padding(MyFarmerTheme.paddings.smallMedium),
                verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
            ) {
                Card(colors = MyFarmerTheme.cardColors.onPrimary) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MyFarmerTheme.paddings.medium),
                        text = stringResource(R.string.select_shop_s_location),
                        style = MyFarmerTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Box(Modifier.weight(1f)) {
                    PickLocationMap(
                        modifier = Modifier.fillMaxWidth(),
                        location = shopInput.location,
                        onLocationSelected = { onShopFormEvent(ShopFormEvent.UpdateLocation(it)) }
                    )
                }

                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.create_shop_map_hint_01),
                        style = MyFarmerTheme.typography.textSmall,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.create_shop_map_hint_02),
                        style = MyFarmerTheme.typography.textSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

private val DEFAULT_LAT_LNG = LatLng(49.8175, 15.4730)
private const val DEFAULT_ZOOM = 6.5f

private const val MAX_ZOOM = 18f

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun PickLocationMap(
    modifier: Modifier = Modifier,
    location: Location?,
    onLocationSelected: (Location) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isMapReady by remember { mutableStateOf(false) }

    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(DEFAULT_LAT_LNG, DEFAULT_ZOOM)
    }

    fun zoomToLocation(latLng: LatLng) = coroutineScope.launch {
        val currentZoom = cameraPositionState.position.zoom
        val newZoom = (currentZoom + 2).coerceAtMost(MAX_ZOOM)
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, newZoom))
    }

    fun zoomFullyToLocation(latLng: LatLng) = coroutineScope.launch {
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, MAX_ZOOM))
    }

    val permission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) {
            permission.launchPermissionRequest()
        }
    }
    val isPermissionGranted = permission.status.isGranted
    LaunchedEffect(isPermissionGranted, isMapReady) {
        when {
            !isPermissionGranted -> {}
            !isMapReady -> {}
            location != null -> zoomFullyToLocation(location.toLatLng())
            else -> locationClient.lastLocation.addOnSuccessListener { newLocation: android.location.Location? ->
                if (newLocation == null) return@addOnSuccessListener

                val latLng = LatLng(newLocation.latitude, newLocation.longitude)
                zoomFullyToLocation(latLng)
                onLocationSelected(Location(latLng))
            }
        }
    }

    Surface(shape = RoundedCornerShape(16.dp)) {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = isPermissionGranted,
                isBuildingEnabled = true,
                mapType = MapType.HYBRID,
            ),
            uiSettings = MapUiSettings(
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
            ),
            onMapClick = { latLng ->
                onLocationSelected(Location(latLng))
                zoomToLocation(latLng)
            },
            onMapLoaded = { isMapReady = true }
        ) {
            location?.toLatLng()?.let { latLng ->
                SimpleMarker(
                    position = latLng,
                    onClick = { zoomToLocation(latLng) }
                )
            }
        }
    }
}

@Composable
fun SimpleMarker(position: LatLng, onClick: () -> Unit) {
    val state = rememberUpdatedMarkerState(position)
    Marker(
        state = state,
        onClick = {
            onClick()
            true
        }
    )
}

@Composable
fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }