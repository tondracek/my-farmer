package com.tondracek.myfarmer.ui.shopfilterdialog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.DistanceUnit
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
internal fun DistanceSection(
    modifier: Modifier = Modifier,
    selectedDistance: Distance?,
    onMaxDistanceChange: (Distance?) -> Unit
) {
    val minKm = 0f
    val maxKm = 50f

    val sliderValue = selectedDistance?.value?.toFloat() ?: minKm

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = when (selectedDistance) {
                    null -> "No filter for max distance"
                    else -> "Max distance: $selectedDistance"
                },
                style = MyFarmerTheme.typography.textSmall
            )
            Button(onClick = { onMaxDistanceChange(null) }) {
                Text("Reset")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                modifier = Modifier.weight(1f),
                value = sliderValue,
                onValueChange = { newValue ->
                    when (newValue <= minKm + 0.01f) {
                        true -> onMaxDistanceChange(null)
                        false -> onMaxDistanceChange(Distance(newValue, DistanceUnit.KM))
                    }
                },
                valueRange = minKm..maxKm,
            )

            Text(text = "km", style = MyFarmerTheme.typography.textSmall)
        }
    }
}