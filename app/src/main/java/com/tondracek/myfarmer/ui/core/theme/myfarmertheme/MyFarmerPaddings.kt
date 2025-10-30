package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

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
    mediumLarge = 20.dp,
    large = 24.dp,
    extraLarge = 32.dp,
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
)
