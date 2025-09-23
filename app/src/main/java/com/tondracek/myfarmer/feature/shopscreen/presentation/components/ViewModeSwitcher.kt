package com.tondracek.myfarmer.feature.shopscreen.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.feature.shopscreen.presentation.ShopsViewMode
import com.tondracek.myfarmer.shared.theme.MyFarmerTheme
import com.tondracek.myfarmer.shared.ui.preview.PreviewApi34Dark
import com.tondracek.myfarmer.shared.ui.preview.PreviewApi34Light

@Composable
fun ViewModeSwitcher(
    modifier: Modifier = Modifier,
    selectedMode: ShopsViewMode,
    onMapClick: () -> Unit,
    onListClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(64.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 4.dp,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ViewModeButton(
                label = stringResource(R.string.map),
                icon = Icons.Filled.Place,
                selected = selectedMode == ShopsViewMode.Map,
                onClick = onMapClick
            )
            ViewModeButton(
                label = stringResource(R.string.list),
                icon = Icons.AutoMirrored.Filled.List,
                selected = selectedMode == ShopsViewMode.List,
                onClick = onListClick
            )
        }
    }
}

@Composable
private fun ViewModeButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "contentColor"
    )

    Button(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .animateContentSize()
            .scale(if (selected) 1f else 0.95f),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = label)
            Text(text = label)
        }
    }
}

@PreviewApi34Light
@Composable
private fun ViewModeSwitcherMapPreview() {
    var selectedMode by remember { mutableStateOf(ShopsViewMode.Map) }
    MyFarmerTheme {
        ViewModeSwitcher(
            selectedMode = selectedMode,
            onMapClick = { selectedMode = ShopsViewMode.Map },
            onListClick = { selectedMode = ShopsViewMode.List },
        )
    }
}

@PreviewApi34Dark
@Composable
private fun ViewModeSwitcherListPreview() {
    var selectedMode by remember { mutableStateOf(ShopsViewMode.List) }
    MyFarmerTheme {
        ViewModeSwitcher(
            selectedMode = selectedMode,
            onMapClick = { selectedMode = ShopsViewMode.Map },
            onListClick = { selectedMode = ShopsViewMode.List },
        )
    }
}