package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val LocalMyFarmerTypography = staticCompositionLocalOf { myFarmerTypography }

val myFarmerTypography = MyFarmerTypography(
    headerMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.W700,
    ),
    headerSmall = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.W500,
    ),
    textLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.W400,
    ),
    textMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.W400,
    ),
    textSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.W400,
    ),
    topbarTitle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.W600,
    ),
)

@Stable
@Immutable
data class MyFarmerTypography(
    val headerMedium: TextStyle,
    val headerSmall: TextStyle,

    val textLarge: TextStyle,
    val textMedium: TextStyle,
    val textSmall: TextStyle,
    val topbarTitle: TextStyle,
)
