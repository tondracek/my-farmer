package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

enum class MyFarmerThemeVariant {
    LIGHT,
    DARK,
}

@Composable
fun MyFarmerThemeConfiguration(
    variant: MyFarmerThemeVariant,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (variant) {
        MyFarmerThemeVariant.LIGHT -> farmerLightColors
        MyFarmerThemeVariant.DARK -> farmerDarkColors
    }

    CompositionLocalProvider(
        LocalMyFarmerColors provides colorScheme,
        LocalMyFarmerTypography provides myFarmerTypography,
        LocalMyFarmerPaddings provides myFarmerPaddings,
    ) {
        content()
    }
}