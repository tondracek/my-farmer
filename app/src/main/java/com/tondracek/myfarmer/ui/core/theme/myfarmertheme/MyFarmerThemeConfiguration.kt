package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerPaddings
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.LocalMyFarmerTypography
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.MyFarmerColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.farmerDarkColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.farmerLightColors
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.myFarmerPaddings
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.myFarmerTypography
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.toMyFarmerColors

enum class MyFarmerThemeVariant {
    LIGHT,
    DARK,
}

@Composable
fun MyFarmerThemeConfiguration(
    variant: MyFarmerThemeVariant,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ->
            getDynamicMaterialColorScheme(darkTheme = variant == MyFarmerThemeVariant.DARK)
                .toMyFarmerColors(variant)

        else -> getFarmerColorScheme(variant)
    }

    CompositionLocalProvider(
        LocalMyFarmerColors provides colorScheme,
        LocalMyFarmerTypography provides myFarmerTypography,
        LocalMyFarmerPaddings provides myFarmerPaddings,
    ) {
        content()
    }
}

fun getFarmerColorScheme(variant: MyFarmerThemeVariant): MyFarmerColors = when (variant) {
    MyFarmerThemeVariant.LIGHT -> farmerLightColors
    MyFarmerThemeVariant.DARK -> farmerDarkColors
}