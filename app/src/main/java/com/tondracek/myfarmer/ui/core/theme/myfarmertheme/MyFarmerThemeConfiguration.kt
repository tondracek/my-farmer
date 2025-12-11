package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerPaddings
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerTypography
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.farmerDarkColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.farmerLightColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.myFarmerPaddings
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.myFarmerTypography

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