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
            containerColor = this.colors.surfaceVariant,
            labelColor = this.colors.onSurfaceVariant,
            selectedContainerColor = this.colors.primaryContainer,
            selectedLabelColor = this.colors.onPrimaryContainer,
        ),
        primary = FilterChipDefaults.filterChipColors(
            containerColor = this.colors.primaryContainer,
            labelColor = this.colors.onPrimaryContainer,
            selectedContainerColor = this.colors.primary,
            selectedLabelColor = this.colors.onPrimary,
        ),
        secondary = FilterChipDefaults.filterChipColors(
            containerColor = this.colors.secondaryContainer,
            labelColor = this.colors.onSecondaryContainer,
            selectedContainerColor = this.colors.secondary,
            selectedLabelColor = this.colors.onSecondary,
        ),
    )