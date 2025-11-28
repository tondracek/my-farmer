package com.tondracek.myfarmer.ui.createshopflow.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation

@Composable
fun MapSelectorPlaceholder(
    selectedLocation: ShopLocation?,
    onLocationSelected: (ShopLocation) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color(0xFFEFF6EF))
            .pointerInput(Unit) {
                detectTapGestures { offset: Offset ->
                    val fake = ShopLocation(
                        49.20 + (offset.x % 1) * 0.001,
                        16.60 + (offset.y % 1) * 0.001,
                    )
                    onLocationSelected(fake)
                }
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedLocation == null) {
            Text("Tap anywhere to place a marker")
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = selectedLocation.toString(),
                    color = Color.DarkGray
                )
            }
        }
    }
}
