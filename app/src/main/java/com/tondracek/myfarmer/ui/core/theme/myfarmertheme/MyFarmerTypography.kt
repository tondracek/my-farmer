package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val LocalMyFarmerTypography = staticCompositionLocalOf { myFarmerTypography }

val myFarmerTypography = MyFarmerTypography(
    header = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
)

@Stable
@Immutable
data class MyFarmerTypography(
    val header: TextStyle,
)
