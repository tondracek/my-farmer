package com.tondracek.myfarmer.ui.core.theme.myfarmertheme

import androidx.compose.runtime.Composable

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

