package com.example.myfarmer.feature.shopscreen.presentation.root.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myfarmer.R
import com.example.myfarmer.feature.shopscreen.presentation.root.ShopViewMode
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.preview.PreviewApi34Dark
import com.example.myfarmer.shared.ui.preview.PreviewApi34Light

@Composable
fun ViewModeSwitcher(
    modifier: Modifier = Modifier,
    selectedMode: ShopViewMode,
    onMapClick: () -> Unit,
    onListClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(64.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ViewModeButton(
                label = stringResource(R.string.map),
                icon = Icons.Default.Check,
                selected = selectedMode == ShopViewMode.Map,
                onClick = onMapClick
            )
            ViewModeButton(
                label = stringResource(R.string.list),
                icon = Icons.AutoMirrored.Filled.List,
                selected = selectedMode == ShopViewMode.List,
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
    val colors = when (selected) {
        true -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )

        false -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    Button(
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 4.dp,
        ),
        onClick = onClick,
        colors = colors,
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
    var selectedMode by remember { mutableStateOf(ShopViewMode.Map) }
    MyFarmerTheme {
        ViewModeSwitcher(
            selectedMode = selectedMode,
            onMapClick = { selectedMode = ShopViewMode.Map },
            onListClick = { selectedMode = ShopViewMode.List },
        )
    }
}

@PreviewApi34Dark
@Composable
private fun ViewModeSwitcherListPreview() {
    var selectedMode by remember { mutableStateOf(ShopViewMode.List) }
    MyFarmerTheme {
        ViewModeSwitcher(
            selectedMode = selectedMode,
            onMapClick = { selectedMode = ShopViewMode.Map },
            onListClick = { selectedMode = ShopViewMode.List },
        )
    }
}