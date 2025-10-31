package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tondracek.myfarmer.ui.core.theme.material.AppTypography
import com.tondracek.myfarmer.ui.core.theme.material.materialDarkScheme
import com.tondracek.myfarmer.ui.core.theme.material.materialLightScheme

data object MyFarmerTheme {
    val colors: MyFarmerColors
        @Composable
        get() = LocalMyFarmerColors.current

    val typography: MyFarmerTypography
        @Composable
        get() = LocalMyFarmerTypography.current

    val paddings: MyFarmerPaddings
        @Composable
        get() = LocalMyFarmerPaddings.current
}

@Composable
fun MyFarmerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> materialDarkScheme
        else -> materialLightScheme
    }

    MyFarmerThemeConfiguration(
        variant = when (darkTheme) {
            true -> MyFarmerThemeVariant.DARK
            false -> MyFarmerThemeVariant.LIGHT
        }
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
