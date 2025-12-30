package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

data class MyFarmerChipColors(
    val base: SelectableChipColors,
    val primary: SelectableChipColors,
    val secondary: SelectableChipColors,
)

val MyFarmerTheme.filterChipColors
    @Composable get() = MyFarmerChipColors(
        base = FilterChipDefaults.filterChipColors(
            containerColor = MyFarmerTheme.colors.surfaceVariant,
            labelColor = MyFarmerTheme.colors.onSurfaceVariant,
            selectedContainerColor = MyFarmerTheme.colors.primaryContainer,
            selectedLabelColor = MyFarmerTheme.colors.onPrimaryContainer,
        ),
        primary = FilterChipDefaults.filterChipColors(
            containerColor = MyFarmerTheme.colors.primaryContainer,
            labelColor = MyFarmerTheme.colors.onPrimaryContainer,
            selectedContainerColor = MyFarmerTheme.colors.primary,
            selectedLabelColor = MyFarmerTheme.colors.onPrimary,
        ),
        secondary = FilterChipDefaults.filterChipColors(
            containerColor = MyFarmerTheme.colors.secondaryContainer,
            labelColor = MyFarmerTheme.colors.onSecondaryContainer,
            selectedContainerColor = MyFarmerTheme.colors.secondary,
            selectedLabelColor = MyFarmerTheme.colors.onSecondary,
        ),
    )