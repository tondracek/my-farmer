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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.DistanceUnit
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
internal fun DistanceSection(
    modifier: Modifier = Modifier,
    selectedDistance: Distance?,
    onMaxDistanceChange: (Distance?) -> Unit
) {
    val preferredUnit = DistanceUnit.KM

    val min = Distance(0f, preferredUnit)
    val max = Distance(50, preferredUnit)

    val sliderValue = selectedDistance?.value?.toFloat() ?: min.value.toFloat()

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = when (selectedDistance) {
                    null -> stringResource(R.string.distance_filter_no_filter)
                    else -> "Max distance: ${selectedDistance.toStringTranslated()}."
                },
                style = MyFarmerTheme.typography.textSmall
            )
            Button(onClick = { onMaxDistanceChange(null) }) {
                Text(stringResource(R.string.reset))
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
                    when (newValue <= min.value.toFloat() + 0.01) {
                        true -> onMaxDistanceChange(null)
                        false -> onMaxDistanceChange(Distance(newValue, preferredUnit))
                    }
                },
                valueRange = min.value.toFloat()..max.value.toFloat(),
            )

            if (selectedDistance != null) Text(
                text = selectedDistance.unit.toStringTranslated(),
                style = MyFarmerTheme.typography.textSmall
            )
        }
    }
}