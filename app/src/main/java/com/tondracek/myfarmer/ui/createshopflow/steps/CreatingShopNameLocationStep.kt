package com.tondracek.myfarmer.ui.createshopflow.steps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.NavigationButtons
import kotlinx.coroutines.launch

@Composable
fun CreatingShopNameLocationStep(
    shopInput: ShopInput,
    onUpdateName: (String) -> Unit,
    onUpdateLocation: (ShopLocation) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Content(
            modifier = Modifier.weight(1f),
            shopInput = shopInput,
            onUpdateName = onUpdateName,
            onUpdateLocation = onUpdateLocation
        )

        NavigationButtons(
            modifier = Modifier.padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = onNextStep,
            onPrevious = onPreviousStep,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    shopInput: ShopInput,
    onUpdateName: (String) -> Unit,
    onUpdateLocation: (ShopLocation) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = shopInput.name,
            onValueChange = onUpdateName,
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

                PickLocationMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    location = shopInput.location,
                    onLocationSelected = onUpdateLocation
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun PickLocationMap(
    modifier: Modifier = Modifier,
    location: ShopLocation?,
    onLocationSelected: (ShopLocation) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val cameraPositionState = rememberCameraPositionState()
    fun zoomToLocation(latLng: LatLng, zoom: Float = 20f) = coroutineScope.launch {
        val currentZoom = cameraPositionState.position.zoom
        if (currentZoom < zoom)
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    val permission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permission.status.isGranted) {
            permission.launchPermissionRequest()
        }
    }
    val isPermissionGranted = permission.status.isGranted
    LaunchedEffect(isPermissionGranted) {
        when {
            !isPermissionGranted -> {}
            location != null -> zoomToLocation(location.toLatLng())
            else -> locationClient.lastLocation.addOnSuccessListener { newLocation: Location? ->
                if (newLocation == null) return@addOnSuccessListener

                val latLng = LatLng(newLocation.latitude, newLocation.longitude)
                zoomToLocation(latLng)
                onLocationSelected(ShopLocation(latLng))
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
            ),
            uiSettings = MapUiSettings(
                rotationGesturesEnabled = false,
            ),
            onMapClick = { latLng ->
                zoomToLocation(latLng)
                onLocationSelected(ShopLocation(latLng))
            }
        ) {
            if (location != null)
                SimpleMarker(position = location.toLatLng())
        }
    }
}

@Composable
fun SimpleMarker(position: LatLng) {
    val state = rememberUpdatedMarkerState(position)
    Marker(state = state)
}

@Composable
fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }