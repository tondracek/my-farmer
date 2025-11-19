package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalMyFarmerPaddings = staticCompositionLocalOf { myFarmerPaddings }

val myFarmerPaddings = MyFarmerPaddings(
    extraSmall = 4.dp,
    small = 8.dp,
    smallMedium = 12.dp,
    medium = 16.dp,
    mediumLarge = 24.dp,
    large = 32.dp,
    extraLarge = 48.dp,
    xxL = 64.dp,
)

@Stable
@Immutable
data class MyFarmerPaddings(
    val extraSmall: Dp,
    val small: Dp,
    val smallMedium: Dp,
    val medium: Dp,
    val mediumLarge: Dp,
    val large: Dp,
    val extraLarge: Dp,
    val xxL: Dp,
)
