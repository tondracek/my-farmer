package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable

data class MyFarmerButtonColors(
    val base: ButtonColors,
    val primary: ButtonColors,
    val secondary: ButtonColors,
    val tertiary: ButtonColors,
    val error: ButtonColors,
)

val myFarmerButtonColors
    @Composable get() = MyFarmerButtonColors(
        base = ButtonDefaults.buttonColors(),
        primary = ButtonDefaults.buttonColors(
            containerColor = MyFarmerTheme.colors.primary,
            contentColor = MyFarmerTheme.colors.onPrimary,
        ),
        secondary = ButtonDefaults.buttonColors(
            containerColor = MyFarmerTheme.colors.secondary,
            contentColor = MyFarmerTheme.colors.onSecondary,
        ),
        tertiary = ButtonDefaults.buttonColors(
            containerColor = MyFarmerTheme.colors.tertiary,
            contentColor = MyFarmerTheme.colors.onTertiary,
        ),
        error = ButtonDefaults.buttonColors(
            containerColor = MyFarmerTheme.colors.error,
            contentColor = MyFarmerTheme.colors.onError,
        ),
    )

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