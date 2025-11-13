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
    smallMedium = 10.dp,
    medium = 12.dp,
    mediumLarge = 14.dp,
    large = 16.dp,
    extraLarge = 24.dp,
    xxL = 32.dp,
    xxxL = 48.dp,
    xxxxL = 64.dp,
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
    val xxxL: Dp,
    val xxxxL: Dp,
)
