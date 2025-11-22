package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable

data class MyFarmerIconButtonColors(
    val base: IconButtonColors,
    val primary: IconButtonColors,
    val secondary: IconButtonColors,
)

val myFarmerIconButtonColors
    @Composable get() = MyFarmerIconButtonColors(
        base = IconButtonDefaults.iconButtonColors(),
        primary = IconButtonDefaults.iconButtonColors(
            containerColor = MyFarmerTheme.colors.primary,
            contentColor = MyFarmerTheme.colors.onPrimary,
        ),
        secondary = IconButtonDefaults.iconButtonColors(
            containerColor = MyFarmerTheme.colors.secondary,
            contentColor = MyFarmerTheme.colors.onSecondary,
        ),
    )