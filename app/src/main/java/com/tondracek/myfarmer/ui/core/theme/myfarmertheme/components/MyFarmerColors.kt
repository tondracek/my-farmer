package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalMyFarmerColors = staticCompositionLocalOf { farmerLightColors }

val farmerLightColors: MyFarmerColors = MyFarmerColors(
    primary = Color(0xFF3C6838),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBDF0B3),
    onPrimaryContainer = Color(0xFF255023),

    secondary = Color(0xFF53634E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E8CE),
    onSecondaryContainer = Color(0xFF3B4B38),

    tertiary = Color(0xFF38656A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBCEBF0),
    onTertiaryContainer = Color(0xFF1E4D52),

    success = Color(0xFF4CAF50),
    onSuccess = Color(0xFFFFFFFF),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),

    background = Color(0xFFF7FBF1),
    onBackground = Color(0xFF191D17),

    surface = Color(0xFFF7FBF1),
    onSurface = Color(0xFF191D17),
    surfaceVariant = Color(0xFFDEE5D8),
    onSurfaceVariant = Color(0xFF424940),
    surfaceTint = Color(0xFF3C6838),

    outline = Color(0xFF73796F),
    outlineVariant = Color(0xFFC2C8BD),
    scrim = Color(0xFF000000),

    inverseSurface = Color(0xFF2D322B),
    inverseOnSurface = Color(0xFFEFF2E9),
    inversePrimary = Color(0xFFA2D399),
    surfaceDim = Color(0xFFD8DBD2),
    surfaceBright = Color(0xFFF7FBF1),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF2F5EB),
    surfaceContainer = Color(0xFFECEFE6),
    surfaceContainerHigh = Color(0xFFE6E9E0),
    surfaceContainerHighest = Color(0xFFE0E4DA),

    line = Color(0xFFE7E7E7),
    ratingStars = Color(0xFFFFC107),

    text = Color(0xFF000000),
    textInvert = Color(0xFFFFFFFF),
)

val farmerDarkColors: MyFarmerColors = MyFarmerColors(
    primary = Color(0xFFA2D399),
    onPrimary = Color(0xFF0C390E),
    primaryContainer = Color(0xFF255023),
    onPrimaryContainer = Color(0xFFBDF0B3),

    secondary = Color(0xFFBACCB3),
    onSecondary = Color(0xFF253423),
    secondaryContainer = Color(0xFF3B4B38),
    onSecondaryContainer = Color(0xFFD6E8CE),

    tertiary = Color(0xFFA0CFD4),
    onTertiary = Color(0xFF00363B),
    tertiaryContainer = Color(0xFF1E4D52),
    onTertiaryContainer = Color(0xFFBCEBF0),

    success = Color(0xFF81C784),
    onSuccess = Color(0xFF152A12),

    error = Color(0xFF93000A),
    onError = Color(0xFFFFDAD6),
    errorContainer = Color(0xFFFFB4AB),
    onErrorContainer = Color(0xFF690005),

    background = Color(0xFF10140F),
    onBackground = Color(0xFFE0E4DA),

    surface = Color(0xFF10140F),
    onSurface = Color(0xFFE0E4DA),
    surfaceVariant = Color(0xFF424940),
    onSurfaceVariant = Color(0xFFC2C8BD),
    surfaceTint = Color(0xFFA2D399),

    outline = Color(0xFF8C9388),
    outlineVariant = Color(0xFF424940),
    scrim = Color(0xFF000000),

    inverseSurface = Color(0xFFE0E4DA),
    inverseOnSurface = Color(0xFF2D322B),
    inversePrimary = Color(0xFF3C6838),
    surfaceDim = Color(0xFF10140F),
    surfaceBright = Color(0xFF363A34),
    surfaceContainerLowest = Color(0xFF0B0F0A),
    surfaceContainerLow = Color(0xFF191D17),
    surfaceContainer = Color(0xFF1D211B),
    surfaceContainerHigh = Color(0xFF272B25),
    surfaceContainerHighest = Color(0xFF323630),

    line = Color(0xFF1F1F1F),
    ratingStars = Color(0xFFFFC107),

    text = Color(0xFFFFFFFF),
    textInvert = Color(0xFF000000),
)

@Stable
@Immutable
data class MyFarmerColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,

    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    val background: Color,
    val onBackground: Color,

    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,

    val success: Color,
    val onSuccess: Color,

    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,

    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,

    val line: Color,
    val ratingStars: Color,

    val text: Color,
    val textInvert: Color,
)